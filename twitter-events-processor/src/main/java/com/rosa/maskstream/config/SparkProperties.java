package com.rosa.maskstream.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties("mask-kafka-consumer")
public class SparkProperties {
    private String AppName;
    private String master;
    private String broker;
    private String topic;
    private long duration;
}
