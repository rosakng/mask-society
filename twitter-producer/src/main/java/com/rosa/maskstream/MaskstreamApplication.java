package com.rosa.maskstream;

import com.rosa.maskstream.config.KafkaConfigProperties;
import com.rosa.maskstream.config.TwitterConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties({KafkaConfigProperties.class, TwitterConfigProperties.class})
public class MaskstreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaskstreamApplication.class, args);
	}
}
