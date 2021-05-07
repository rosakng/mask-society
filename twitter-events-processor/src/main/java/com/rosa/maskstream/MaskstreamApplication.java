package com.rosa.maskstream;

import com.rosa.maskstream.consumer.SparkStream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MaskstreamApplication implements CommandLineRunner{

	private final SparkStream sparkStream;

	public MaskstreamApplication(SparkStream sparkStream) {
		this.sparkStream = sparkStream;
	}

	public static void main(String[] args) {
		SpringApplication.run(MaskstreamApplication.class, args);
	}

	@Override
	public void run(String... args) {
		sparkStream.run();
	}

}
