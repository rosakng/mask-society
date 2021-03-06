package com.rosa.maskstream.consumer;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rosa.maskstream.config.KafkaConsumerConfig;
import com.rosa.maskstream.config.KafkaProperties;
import com.rosa.maskstream.external.TextSimilarity;
import com.rosa.maskstream.model.Tweet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Optional;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;
import static com.rosa.maskstream.support.Constants.*;

@Service
@AllArgsConstructor
@Slf4j
public class MaskTweetsStreamProcessor {

    private final KafkaConsumerConfig kafkaConsumerConfig;
    private final KafkaProperties kafkaProperties;

    public void run() throws InterruptedException {
        SparkConf twitterSparkConfig = new SparkConf()
                .setAppName("twitterKafka")
                .setMaster("local[2]")
                .set("spark.executor.memory", "1g");

        JavaStreamingContext javaStreamingContext =
                new JavaStreamingContext(twitterSparkConfig, Durations.seconds(kafkaProperties.getBatchInterval()));

        JavaDStream<ConsumerRecord<String, String>> kafkaStream = KafkaUtils.createDirectStream(
                javaStreamingContext,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(
                        Collections.singletonList(kafkaProperties.getTopic()), kafkaConsumerConfig.consumerConfigs()));

        JavaDStream<Tweet> tweetJavaDStream = kafkaStream.map(consumerRecord -> {
            log.info("CONSUMER RECORD: {}", consumerRecord.value());
            JsonNode streamPayload = new ObjectMapper().readTree(consumerRecord.value());
            log.info("STREAM PAYLOAD: {}", streamPayload.toString());
            JsonNode userPayload = streamPayload.get("user");
            log.info("USER PAYLOAD: {}", userPayload.toString());
            JsonNode location = userPayload.get("location");
            JsonNode text = streamPayload.get("text");
            if ((location == null || StringUtils.isEmpty(location.toString()) || location.toString().equals("null"))) {
                return null;
            }

            String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]"; //removes symbols and emojis
            String locationText = location.toString().replaceAll(characterFilter, "").toLowerCase();
            log.info("LOCATION TEXT: {}", locationText);

            Optional<String> standardLocation = Optional.empty();
            for (String place : LOCATIONS) {
                if (locationText.contains(place)) {
                    standardLocation = Optional.of(place);
                }
            }

            if (!standardLocation.isPresent()) {
                return null;
            }

            String cleanedTweetText = text.toString().replaceAll(characterFilter, "");
            log.info("CLEAN TWEET " + cleanedTweetText);
            return new Tweet(
                    UUIDs.timeBased(),
                    streamPayload.get("created_at").toString(),
                    userPayload.get("screen_name").toString(),
                    standardLocation.get(),
                    cleanedTweetText,
                    "");
        }).filter(object ->
                object != null && !StringUtils.isEmpty(object.getLocation()) && !object.getLocation().equals("null"))
                .map(tweet -> {
                    TextSimilarity.processCosineSimIntoBuckets(tweet);
                    return tweet;
                });

        tweetJavaDStream.foreachRDD(tweetJavaRDD -> {
            log.info("WRITING TO DB");
            javaFunctions(tweetJavaRDD).writerBuilder(
                    TWEET_KEYSPACE_NAME,
                    TWEET_TABLE_NAME,
                    mapToRow(Tweet.class))
                    .saveToCassandra();
        });

        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }
}
