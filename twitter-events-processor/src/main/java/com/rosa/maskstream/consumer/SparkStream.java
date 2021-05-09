package com.rosa.maskstream.consumer;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rosa.maskstream.config.ApiProperties;
import com.rosa.maskstream.config.KafkaProperties;
import com.rosa.maskstream.externalApi.Api;
import com.rosa.maskstream.model.Tweet;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.*;

@Service
public class SparkStream {

//    private final KafkaConsumerConfig kafkaConsumerConfig;
    private final KafkaProperties kafkaProperties;
    private final ApiProperties apiProperties;

    public SparkStream(KafkaProperties kafkaProperties, ApiProperties apiProperties) {
//        this.kafkaConsumerConfig = kafkaConsumerConfig;
        this.kafkaProperties = kafkaProperties;
        this.apiProperties = apiProperties;
    }

    public void run () throws InterruptedException {
        SparkConf twitterSparkConfig = new SparkConf()
                .setAppName("twitterKafka")
                .setMaster("local[2]")
                .set("spark.executor.memory", "1g");

        System.out.println("TWITTER CONF: " + twitterSparkConfig.toString());
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(twitterSparkConfig, Durations.seconds(20));

        HashMap<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootStrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getGroupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getAutoOffset());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        JavaDStream<ConsumerRecord<String, String>> kafkaStream = KafkaUtils.createDirectStream(
                javaStreamingContext,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.<String, String>Subscribe(Arrays.asList(kafkaProperties.getTopic()), props));

        JavaDStream<Tweet> tweetJavaDStream = kafkaStream.map(consumerRecord -> {
//            JSONObject tweetObj = (JSONObject) new JSONParser().parse(consumerRecord.value());
//            JSONObject valueObj = (JSONObject) tweetObj.get("value");
//            JSONObject userObj = (JSONObject) valueObj.get("user");
            System.out.println("RECORD: " + consumerRecord.value());
            JsonNode streamPayload = new ObjectMapper().readTree(consumerRecord.value());
            System.out.println("STREAM PAYLOAD: "+ streamPayload.toString());
            JsonNode userPayload = streamPayload.get("user");
            System.out.println("USER PAYLOAD: "+ userPayload.toString());
            if (userPayload.get("location")!= null || !StringUtils.isEmpty(userPayload.get("location").toString()) || !userPayload.get("location").toString().equals("null")) {
                return new Tweet(
                        UUIDs.timeBased(),
                        streamPayload.get("created_at").textValue(),
                        userPayload.get("screen_name").toString(),
                        userPayload.get("location").toString(),
                        streamPayload.get("text").toString(),
                        "-1.0");
            }
            return null;
        });

        tweetJavaDStream
                .filter(obj -> obj != null && obj.getLocation() != null)
//                .foreachRDD(rdd -> {
//                    rdd.foreachPartition(partitionOfRecords -> {
//                        RequestConfig requestConfig = RequestConfig.custom()
//                                .setConnectionRequestTimeout(6000)
//                                .setConnectTimeout(10000)
//                                .setSocketTimeout(10000)
//                                .build();
//                        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//                        HttpPost post = new HttpPost("https://api.cohere.ai/baseline-squid/similarity");
//                        post.setConfig(requestConfig);
//                        partitionOfRecords.forEachRemaining(record -> {
//                            String json = "{\"anchor\":" + "\"masks are taking away freedom\"" +
//                                    ",\"targets\":[" + record.getTweetText() + "]}";
//
//                            System.out.println("REQUEST JSON: " + json);
//                            StringEntity entity = null;
//                            try {
//                                entity = new StringEntity(json);
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
//                            post.setEntity(entity);
//                            post.setHeader("Accept", "application/json");
//                            post.setHeader("Content-type", "application/json");
//                            post.setHeader("Authorization", "Bearer: E5TUGcS6shd411RUM96vgRVb1C2JmDfhMAlNQZ5X");
//                            try {
//                                CloseableHttpResponse httpResponse = httpClient.execute(post);
////                                Thread.sleep(5000);
//                                String response = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
//                                System.out.println("HTTP RESPONSE: "+ response);
//                                JsonNode jsonNode = new ObjectMapper().readTree(response);
//                                String split = jsonNode.get("similarities").toString();
//                                record.setSimScore(split.substring(1, split.length()-1));
//                            } catch (Exception e) {
//                                System.out.println("THERE WAS A PROBLEM");
//                                e.printStackTrace();
//
//                            }
//                            System.out.println("FINALIZED TWEET" + record.toString());
//                        });
//                        httpClient.close();
//                    });

        .map(tweet -> {
            tweet.setSimScore("test");
            return tweet;
        });


        tweetJavaDStream.foreachRDD(tweetJavaRDD -> {
            System.out.println("WRITING TO DB");
            javaFunctions(tweetJavaRDD).writerBuilder(
                    CassandraTweetWriter.TWEET_KEYSPACE_NAME,
                    CassandraTweetWriter.TWEET_TABLE_NAME,
                    mapToRow(Tweet.class))
                    .saveToCassandra();
        });

        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }
}
