package com.rosa.maskstream.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapTupleToRow;
import static com.rosa.maskstream.support.Constants.*;

@AllArgsConstructor
@Slf4j
public class TweetAggregatorCron {
    public static final String SIM_SCORE_TABLE = "sim_score_table";

    private final CassandraConnector cassandraConnector = new CassandraConnector();

    private static final List<String> SCHEMA_PARAMS = Arrays.asList(
            "location_similarity text",
            "num_occurrences int",
            "PRIMARY KEY (location_similarity)"
    );

    public void execute() {
        log.info("INITIALIZING PERIODIC ROLLUP TASK");
        cassandraConnector.connect("127.0.0.1", 9042);
        cassandraConnector.createKeyspace(TWEET_KEYSPACE_NAME, REPLICATION_STRATEGY, REPLICATION_FACTOR);
        cassandraConnector.createTable(TWEET_KEYSPACE_NAME, SIM_SCORE_TABLE, SCHEMA_PARAMS);
        runAggregatorTask();
        cassandraConnector.close();
    }

    private void runAggregatorTask() {
        log.info("RUNNING CRON AGGREGATOR TASK");
        SparkConf twitterSparkConfig = new SparkConf()
                .setAppName("twitterKafka")
                .setMaster("local[2]")
                .set("spark.executor.memory", "1g");
        JavaSparkContext sparkContext = new JavaSparkContext(twitterSparkConfig);

        JavaPairRDD<String, Long> cassandraRdd = javaFunctions(sparkContext)
                .cassandraTable(TWEET_KEYSPACE_NAME, TWEET_TABLE_NAME)
                .select("location", "cos_sim_bucket")
                .where("id < minTimeuuid(?)", new Date())
                .mapToPair((s) -> new Tuple2<>(s.getString(0) + " " + s.getString(1), (long) 1))
                .reduceByKey(Long::sum);

        javaFunctions(cassandraRdd).writerBuilder(
                TWEET_KEYSPACE_NAME, SIM_SCORE_TABLE, mapTupleToRow(String.class, Long.class)).saveToCassandra();
    }
}
