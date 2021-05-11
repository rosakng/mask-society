package com.rosa.maskstream.consumer;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.rosa.maskstream.support.Constants.*;

@Component
@NoArgsConstructor
@Slf4j
public class CassandraTweetWriter {

    private static final List<String> TWEET_TABLE_SCHEMA_PARAMS = Arrays.asList(
            "id timeuuid",
            "created_at text",
            "username text",
            "location text",
            "tweet_text text",
            "cos_sim_bucket text",
            "PRIMARY KEY (id)");

    private final CassandraConnector cassandraConnector = new CassandraConnector();

    public void run() {
        log.info("KAFKA INITIALIZATION");
        cassandraConnector.connect("127.0.0.1", null);
        cassandraConnector.createKeyspace(TWEET_KEYSPACE_NAME, REPLICATION_STRATEGY, REPLICATION_FACTOR);
        cassandraConnector.createTable(TWEET_KEYSPACE_NAME, TWEET_TABLE_NAME, TWEET_TABLE_SCHEMA_PARAMS);
        cassandraConnector.close();
    }
}
