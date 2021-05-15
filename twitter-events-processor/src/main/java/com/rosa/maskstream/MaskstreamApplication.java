package com.rosa.maskstream;

import com.rosa.maskstream.consumer.CassandraTweetWriter;
import com.rosa.maskstream.consumer.MaskTweetsStreamProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MaskstreamApplication implements CommandLineRunner {

	private final MaskTweetsStreamProcessor maskTweetsStreamProcessor;
	private final CassandraTweetWriter cassandraTweetWriter;

	public MaskstreamApplication(MaskTweetsStreamProcessor maskTweetsStreamProcessor, CassandraTweetWriter cassandraTweetWriter) {
		this.maskTweetsStreamProcessor = maskTweetsStreamProcessor;
		this.cassandraTweetWriter = cassandraTweetWriter;
	}

	public static void main(String[] args) {
		SpringApplication.run(MaskstreamApplication.class, args);
	}

	@Override
	public void run(String... args) throws InterruptedException {
		cassandraTweetWriter.run();
		maskTweetsStreamProcessor.run();
	}
}
