package com.rosa.maskstream.consumer.kafka;

import com.rosa.maskstream.config.KafkaProperties;
import org.springframework.stereotype.Component;

public class KafkaConsumerConfig {

    private KafkaProperties kafkaProperties;

    public KafkaConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

}
