package com.rosa.maskstream.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("mask-twitter")
public class TwitterConfigProperties {
    private String apiKey;
    private String apiSecretKey;
    private String tokenSecret;
    private String accessToken;

    private KafkaTemplate kafkaTemplate;
}
