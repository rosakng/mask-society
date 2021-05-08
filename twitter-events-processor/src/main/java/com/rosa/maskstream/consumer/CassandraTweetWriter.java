package com.rosa.maskstream.consumer;

import java.util.List;

public class CassandraTweetWriter {

    private static final String TWEET_KEYSPACE_NAME ="maskStream";
    private static final String REPLICATION_STRATEGY = "SimpleStrategy";
    private static final int REPLICATION_FACTOR = 1;

    private static final String TWEET_TABLE_NAME ="tweets";
    private static final List<String> SCHEMA_PARAMS = List.of(
            "id timeuuid",
            "tweet_text text",
            "location text",
            "username text",
            "sim_score text",
            "PRIMARY KEY (id)");

    CassandraConnector cassandraConnector = new CassandraConnector();
    public void run () {
        cassandraConnector.connect("", 2);
        cassandraConnector.createKeyspace(TWEET_KEYSPACE_NAME, REPLICATION_STRATEGY, REPLICATION_FACTOR);
        cassandraConnector.createTable(TWEET_KEYSPACE_NAME, TWEET_TABLE_NAME, SCHEMA_PARAMS);
        cassandraConnector.close();
    }
}
