package com.rosa.maskstream.consumer;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CassandraConnector {

    private Cluster cluster;
    private Session session;

    public void connect(String node, Integer port) {
        Cluster.Builder clusterBuilder = Cluster.builder().addContactPoint(node);

        if (port != null) {
            clusterBuilder.withPort(port);
        }

        cluster = clusterBuilder.build();

        session = cluster.connect();
    }

    public Session getSession() {
        return this.session;
    }

    public void close() {
        session.close();
        cluster.close();
    }

    public void createKeyspace(String keyspaceName, String replicationStrategy, int replicationFactor) {

        String query = "CREATE KEYSPACE IF NOT EXISTS " +
                keyspaceName +
                " WITH replication = {" +
                "'class':'" + replicationStrategy +
                "','replication_factor':" + replicationFactor +
                "};";
        log.info("Executing query: {}", query);
        session.execute(query);
    }


    public void createTable(String keyspaceName, String tableName, List<String> columnNames) {
        String columns = String.join(",", columnNames);

        String query = "CREATE TABLE IF NOT EXISTS " +
                keyspaceName + "." +
                tableName + "(" +
                columns +
                ");";

        log.info("Creating table: {}", query);
        session.execute(query);
    }

}
