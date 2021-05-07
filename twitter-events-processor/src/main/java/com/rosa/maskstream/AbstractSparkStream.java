package com.rosa.maskstream;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.boot.CommandLineRunner;

import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractSparkStream implements CommandLineRunner {

    public AbstractSparkStream() {
    }

    public abstract JavaStreamingContext getJavaStreamingContext();
    public abstract Map<String, Object> getKafkaConsumerConfigProperties();
    public abstract List<String> getTopics();

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting {}", this.getClass().getSimpleName());

        JavaStreamingContext javaStreamingContext = getJavaStreamingContext();
        Map<String, Object> kafkaParams = getKafkaConsumerConfigProperties();
        List<String> topics = getTopics();

        JavaInputDStream<ConsumerRecord<String, String>> twitterMessages = KafkaUtils.createDirectStream(
                javaStreamingContext,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topics, kafkaParams));

        log.info(String.valueOf(twitterMessages));
        System.out.println(String.valueOf(twitterMessages));

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
