package com.rosa.maskstream.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("mask-kafka")
public class KafkaConfigProperties {
    public String maskTopic;
    private String bootStrapServers;
    private String acksConfig;
    private String retriesConfig;
}
