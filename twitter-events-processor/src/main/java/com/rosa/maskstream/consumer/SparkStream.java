package com.rosa.maskstream.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.rosa.maskstream.config.KafkaConsumerConfig;
import com.rosa.maskstream.config.KafkaProperties;
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
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class SparkStream {

    private final KafkaConsumerConfig kafkaConsumerConfig;
    private final KafkaProperties kafkaProperties;

    public SparkStream(KafkaConsumerConfig kafkaConsumerConfig, KafkaProperties kafkaProperties) {
        super();
        this.kafkaConsumerConfig = kafkaConsumerConfig;
        this.kafkaProperties = kafkaProperties;
    }

    public void run () {
        SparkConf twitterSparkConfig = new SparkConf()
                .setAppName("twitterKafka")
                .setMaster("local[2]")
                .set("spark.executor.memory", "1g");

        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(twitterSparkConfig, Durations.seconds(10));

        JavaInputDStream<ConsumerRecord<String, String>> tweetStream = KafkaUtils.createDirectStream(
                javaStreamingContext,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(ImmutableList.of(kafkaProperties.getTopic()), kafkaConsumerConfig.consumerConfigs()));

        JavaDStream<Tweet> lines = tweetStream.map(consumerRecord -> {
            JsonNode streamPayload = new ObjectMapper().readTree(consumerRecord.value());
            JsonNode tweetPayload = streamPayload.get("value");
            JsonNode userPayload = tweetPayload.get("user");

            String id = tweetPayload.get("id").toString();
            String createdAt = tweetPayload.get("created_at").textValue();
            String userName = userPayload.get("screen_name").textValue();
            String location = userPayload.get("location").textValue();
            String text = tweetPayload.get("text").textValue();
            //need to call api and get cosine
            String cosine_sim = ""
            Tweet tweet = new Tweet(id, createdAt, userName, location, text, cosine_sim);
        }).filter(Objects::nonNull).map();
        //Count the tweets and print
        lines.count()
                .map(cnt -> "Popular hash tags in last 60 seconds (" + cnt + " total tweets):")
                .print();

        //
        lines
                .mapToPair(hashTag -> new Tuple2<>(hashTag, 1))
                .reduceByKey((a, b) -> Integer.sum(a, b))
                .mapToPair(stringIntegerTuple2 -> stringIntegerTuple2.swap())
                .foreachRDD(rrdd -> {
                    log.info("------------------------------------------------");
                    //Counts
                    rrdd.sortByKey(false).collect()
                            .forEach(record -> {
                                System.out.println(String.format(" %s (%d)", record._2, record._1));
                            });
                });

        // Start the computation
        javaStreamingContext.start();
        try {
            javaStreamingContext.awaitTermination();
        } catch (InterruptedException e) {
            log.error("Interrupted: {}", e);
            // Restore interrupted state...
        }
    }
}
