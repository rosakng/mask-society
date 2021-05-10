package com.rosa.maskstream.consumer;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rosa.maskstream.config.KafkaConsumerConfig;
import com.rosa.maskstream.config.KafkaProperties;
import com.rosa.maskstream.externalApi.Api;
import com.rosa.maskstream.model.Tweet;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

@Service
@AllArgsConstructor
public class SparkStream {

    private final KafkaConsumerConfig kafkaConsumerConfig;
    private final KafkaProperties kafkaProperties;

    public void run() throws InterruptedException {
        SparkConf twitterSparkConfig = new SparkConf()
                .setAppName("twitterKafka")
                .setMaster("local[2]")
                .set("spark.executor.memory", "1g");

        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(twitterSparkConfig, Durations.seconds(kafkaProperties.getBatchInterval()));

        JavaDStream<ConsumerRecord<String, String>> kafkaStream = KafkaUtils.createDirectStream(
                javaStreamingContext,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(Collections.singletonList(kafkaProperties.getTopic()), kafkaConsumerConfig.consumerConfigs()));

        JavaDStream<Tweet> tweetJavaDStream = kafkaStream.map(consumerRecord -> {
            System.out.println("CONSUMER RECORD: " + consumerRecord.value());
            JsonNode streamPayload = new ObjectMapper().readTree(consumerRecord.value());
            System.out.println("STREAM PAYLOAD: " + streamPayload.toString());
            JsonNode userPayload = streamPayload.get("user");
            System.out.println("USER PAYLOAD: " + userPayload.toString());
            return new Tweet(
                    UUIDs.timeBased(),
                    streamPayload.get("created_at").textValue(),
                    userPayload.get("screen_name").toString(),
                    userPayload.get("location").toString(),
                    streamPayload.get("text").toString(),
                    -1.0);
        }).filter(Objects::nonNull)
                .map(tweet -> {
                    Api.calculateSimilarity(tweet);
                    return tweet;
                });

        tweetJavaDStream.foreachRDD(tweetJavaRDD -> {
            System.out.println("WRITING TO DB");
            javaFunctions(tweetJavaRDD).writerBuilder(
                    CassandraTweetWriter.TWEET_KEYSPACE_NAME,
                    CassandraTweetWriter.TWEET_TABLE_NAME,
                    mapToRow(Tweet.class))
                    .saveToCassandra();
        });

        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }
}
