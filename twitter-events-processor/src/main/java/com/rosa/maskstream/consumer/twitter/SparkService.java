package com.rosa.maskstream.consumer.twitter;

import com.rosa.maskstream.config.KafkaProperties;
import com.rosa.maskstream.consumer.kafka.KafkaConsumerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.jvnet.hk2.annotations.Service;

import java.util.Collections;

@Service
@Slf4j
public class SparkService {
    // This is the service that handles spark

    private final SparkConf sparkConf;
    private final KafkaConsumerConfig kafkaConsumerConfig;
    private final KafkaProperties kafkaProperties;

    public SparkService(SparkConf sparkConf, KafkaConsumerConfig kafkaConsumerConfig, KafkaProperties kafkaProperties) {
        this.sparkConf = sparkConf;
        this.kafkaConsumerConfig = kafkaConsumerConfig;
        this.kafkaProperties = kafkaProperties;
    }

    public void run() {
        log.info("Starting {}", this.getClass().getSimpleName());

        JavaStreamingContext javaStreamingContext =
                new JavaStreamingContext(sparkConf, Durations.seconds(kafkaProperties.getBatchInterval()));

        JavaInputDStream<ConsumerRecord<String, String>> twitterMessages = KafkaUtils.createDirectStream(
                javaStreamingContext,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(Collections.singletonList(kafkaProperties.getTopic()), kafkaConsumerConfig.KafkaConsumerConfigs()));
    }
}
