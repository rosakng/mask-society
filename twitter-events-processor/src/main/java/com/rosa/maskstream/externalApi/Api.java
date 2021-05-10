package com.rosa.maskstream.externalApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rosa.maskstream.model.Tweet;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Api implements Serializable {

    public static final String ANCHOR = "masks are taking away freedom";

    public static void post(Tweet tweet) throws IOException {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(10000)
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://api.cohere.ai/baseline-squid/similarity");
        post.setConfig(requestConfig);
        String json = "{\"anchor\":" + "\"masks are taking away freedom\"" +
                ",\"targets\":[" + tweet.getTweetText() + "]}";

        System.out.println("REQUEST JSON: " + json);
        StringEntity entity = null;
        try {
            entity = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setEntity(entity);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        post.setHeader("Authorization", "Bearer: E5TUGcS6shd411RUM96vgRVb1C2JmDfhMAlNQZ5X");
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            String response = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            System.out.println("HTTP RESPONSE: " + response);
            JsonNode jsonNode = new ObjectMapper().readTree(response);
            String split = jsonNode.get("similarities").toString();
            tweet.setSimScore(Double.valueOf(split.substring(1, split.length() - 1)));
        } catch (Exception e) {
            System.out.println("THERE WAS A PROBLEM");
            e.printStackTrace();

        }
        System.out.println("FINALIZED TWEET" + tweet.toString());
        httpClient.close();
    }

    public static void calculateSimilarity(Tweet tweet) {
        String s1 = tweet.getTweetText();

        Map<CharSequence, Integer> vector1 = new HashMap<>();
        Map<CharSequence, Integer> vector2 = new HashMap<>();

        for (String token : s1.split(" ")) {
            vector1.put(token, vector1.getOrDefault(token, 0) + 1);
        }

        for (String token : ANCHOR.split(" ")) {
            vector2.put(token, vector2.getOrDefault(token, 0) + 1);
        }
        CosineSimilarity cosine = new CosineSimilarity();

        System.out.println("FINALIZED TWEET" + tweet.toString());
        tweet.setSimScore(cosine.cosineSimilarity(vector1, vector2));
    }

}
