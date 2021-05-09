package com.rosa.maskstream.consumer;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

public class CassandraTweetWriter {

    public static final String TWEET_KEYSPACE_NAME ="maskstream";
    public static final String TWEET_TABLE_NAME ="testtweets";

    private static final String REPLICATION_STRATEGY = "SimpleStrategy";
    private static final int REPLICATION_FACTOR = 1;

    private static final List<String> SCHEMA_PARAMS = Arrays.asList(
            "id timeuuid",
            "created_at text",
            "username text",
            "location text",
            "tweet_text text",
            "sim_score text",
            "PRIMARY KEY (id)");

    private final CassandraConnector cassandraConnector = new CassandraConnector();

    public CassandraTweetWriter() {
    }

    public void run () {
        System.out.println("KAFKA INITIALIZATION");
        cassandraConnector.connect("127.0.0.1", null);
        cassandraConnector.createKeyspace(TWEET_KEYSPACE_NAME, REPLICATION_STRATEGY, REPLICATION_FACTOR);
        cassandraConnector.createTable(TWEET_KEYSPACE_NAME, TWEET_TABLE_NAME, SCHEMA_PARAMS);
        cassandraConnector.close();
    }
}
