package com.rosa.maskstream;

import com.rosa.maskstream.config.KafkaProperties;
import com.rosa.maskstream.config.TwitterConfigProperties;
import com.rosa.maskstream.consumer.twitter.SparkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties({KafkaProperties.class, TwitterConfigProperties.class})
@Slf4j
public class MaskstreamApplication implements CommandLineRunner {

	private SparkService sparkService;

	public MaskstreamApplication(SparkService sparkService){
		this.sparkService = sparkService;
	}

	public static void main(String[] args) {
		SpringApplication.run(MaskstreamApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		sparkService.run();
	}
}
