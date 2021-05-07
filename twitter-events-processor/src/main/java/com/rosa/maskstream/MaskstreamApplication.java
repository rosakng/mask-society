package com.rosa.maskstream;

import com.rosa.maskstream.config.KafkaProperties;
import com.rosa.maskstream.consumer.twitter.SparkStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({KafkaProperties.class})
@Slf4j
public class MaskstreamApplication {

	public MaskstreamApplication(){
	}

	public static void main(String[] args) {
		SpringApplication.run(MaskstreamApplication.class, args);
	}

	@Bean
	public SparkStream sparkStream(KafkaProperties kafkaProperties) {
		return new SparkStream(kafkaProperties);
	}
}
