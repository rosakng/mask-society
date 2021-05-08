package com.rosa.maskstream.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "kafka.consumer", ignoreUnknownFields = false)
@Component
@Getter
@Setter
public class ApiProperties {
    private String url;
    private String token;
    private String anchor;
}
