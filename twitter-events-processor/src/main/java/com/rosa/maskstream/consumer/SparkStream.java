package com.rosa.maskstream.consumer;

import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.rosa.maskstream.config.ApiProperties;
import com.rosa.maskstream.config.KafkaConsumerConfig;
import com.rosa.maskstream.config.KafkaProperties;
import com.rosa.maskstream.externalApi.Api;
import com.rosa.maskstream.model.Tweet;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

@Service
@Slf4j
public class SparkStream {

    private final KafkaConsumerConfig kafkaConsumerConfig;
    private final KafkaProperties kafkaProperties;
    private final ApiProperties apiProperties;

    public SparkStream(KafkaConsumerConfig kafkaConsumerConfig, KafkaProperties kafkaProperties, ApiProperties apiProperties) {
        super();
        this.kafkaConsumerConfig = kafkaConsumerConfig;
        this.kafkaProperties = kafkaProperties;
        this.apiProperties = apiProperties;
    }

    public void run () {
        SparkConf twitterSparkConfig = new SparkConf()
                .setAppName("twitterKafka")
                .setMaster("local[2]")
                .set("spark.executor.memory", "1g");

        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(twitterSparkConfig, Durations.seconds(10));

        JavaInputDStream<ConsumerRecord<String, String>> kafkaStream = KafkaUtils.createDirectStream(
                javaStreamingContext,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(ImmutableList.of(kafkaProperties.getTopic()), kafkaConsumerConfig.consumerConfigs()));

        Api api = new Api(apiProperties);

        JavaDStream<Tweet> tweetJavaDStream = kafkaStream.map(consumerRecord -> {
            JsonNode streamPayload = new ObjectMapper().readTree(consumerRecord.value());
            JsonNode tweetPayload = streamPayload.get("value");
            JsonNode userPayload = tweetPayload.get("user");

            return Tweet.builder()
                    .id(tweetPayload.get("id").toString())
                    .createdAt(tweetPayload.get("created_at").textValue())
                    .userName(userPayload.get("screen_name").toString())
                    .location(userPayload.get("location").toString())
                    .text(tweetPayload.get("text").toString())
                    .build();
        })
                .filter(Objects::nonNull)
                .map(tweet -> {
                    Double similarityScore = api.getSimilarityScore(tweet.getText());
                    tweet.setCosineSimilarity(similarityScore);
                    return tweet;
                });

        tweetJavaDStream.foreachRDD(tweetJavaRDD -> CassandraJavaUtil
                .javaFunctions(tweetJavaRDD)
                .writerBuilder(
                        CassandraTweetWriter.TWEET_KEYSPACE_NAME,
                        CassandraTweetWriter.TWEET_TABLE_NAME,
                        mapToRow(Tweet.class))
                .saveToCassandra());


        javaStreamingContext.start();
        try {
            javaStreamingContext.awaitTermination();
        } catch (InterruptedException e) {
            log.error("Interrupted: {}", e);
        }
    }
}
