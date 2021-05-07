package com.rosa.maskstream;

import com.rosa.maskstream.config.KafkaProperties;
import com.rosa.maskstream.config.TwitterConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties({KafkaProperties.class, TwitterConfigProperties.class})
@ComponentScan
@Slf4j
public class MaskstreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaskstreamApplication.class, args);
	}

}
