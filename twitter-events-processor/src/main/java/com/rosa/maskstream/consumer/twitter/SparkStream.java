package com.rosa.maskstream.consumer.twitter;

import com.rosa.maskstream.config.KafkaProperties;
import com.rosa.maskstream.consumer.AbstractSparkStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SparkStream extends AbstractSparkStream {

    private final KafkaProperties kafkaProperties;

    public SparkStream(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Override
    public JavaStreamingContext getJavaStreamingContext() {
        SparkConf twitterSparkConfig = new SparkConf()
                .setAppName("twitterKafka")
                .setMaster("local[2]")
                .set("spark.executor.memory", "1g");
        return new JavaStreamingContext(twitterSparkConfig, Durations.seconds(kafkaProperties.getBatchInterval()));
    }

    @Override
    public Map<String, Object> getKafkaConsumerConfigProperties() {
        return new HashMap<>() {{
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootStrapServers());
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getGroupId());
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getAutoOffset());
            put(ConsumerConfig.CLIENT_ID_CONFIG, kafkaProperties.getClientId());
        }};
    }

    @Override
    public List<String> getTopics() {
        return List.of(kafkaProperties.getTopic());
    }
}
