package com.rosa.maskstream.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("kafka.consumer")
@Getter
@Setter
public class KafkaProperties {
    private String topic;
    private String bootStrapServers;
    private String groupId;
    private String clientId;
    private String autoOffset;
    private long batchInterval;
}
