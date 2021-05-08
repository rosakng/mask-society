package com.rosa.maskstream;

import com.rosa.maskstream.consumer.CassandraTweetWriter;
import com.rosa.maskstream.consumer.SparkStream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MaskstreamApplication implements CommandLineRunner{

	private final SparkStream sparkStream;
	private final CassandraTweetWriter cassandraTweetWriter;

	public MaskstreamApplication(SparkStream sparkStream, CassandraTweetWriter cassandraTweetWriter) {
		this.sparkStream = sparkStream;
		this.cassandraTweetWriter = cassandraTweetWriter;
	}

	public static void main(String[] args) {
		SpringApplication.run(MaskstreamApplication.class, args);
	}

	@Override
	public void run(String... args) {
		cassandraTweetWriter.run();
		sparkStream.run();
	}

}
