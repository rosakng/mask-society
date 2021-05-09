package com.rosa.maskstream.externalApi;

import com.datastax.driver.mapping.annotations.Transient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rosa.maskstream.config.ApiProperties;
import com.rosa.maskstream.model.Tweet;
import org.json.simple.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.DatatypeConverter;


@SuppressWarnings("unchecked")
//@Slf4j
public class Api {

    static class getSimilarityScore implements Serializable {
        final URI uri;
        final Map<String, List<Object>> headers;
        final Serializable entity;

        public getSimilarityScore(URI uri, Map<String, List<Object>> headers, Serializable entity) {
            this.uri = uri;
            this.headers = headers;
            this.entity = entity;
        }

        public Response post(javax.ws.rs.client.Client client) {
            MultivaluedHashMap<String, Object> multimap = new MultivaluedHashMap<String, Object>();
            headers.forEach((k,v) -> multimap.put(k, v));

            return client.target(uri)
                    .request()
                    .headers(multimap)
                    .post(Entity.text(entity));
        }

//        HttpURLConnection con = (HttpURLConnection)url.openConnection();
//        con.setRequestMethod("POST");
//        con.setRequestProperty("Content-Type", "application/json; utf-8");
//        con.setRequestProperty("Accept", "application/json");
//        con.setRequestProperty("Authorization", "Bearer: " + apiProperties.getToken());
//        con.setDoOutput(true);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("anchor", apiProperties.getAnchor());
//        jsonObject.put("targets", Arrays.asList(tweet.getTweetText()));
//
//
//        try(OutputStream os = con.getOutputStream()) {
//            byte[] input = jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8);
//            os.write(input, 0, input.length);
//        }
//
//        try(BufferedReader br = new BufferedReader(
//                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
//            StringBuilder response = new StringBuilder();
//            String responseLine;
//            while ((responseLine = br.readLine()) != null) {
//                response.append(responseLine.trim());
//            }
//            JsonNode jsonNode = new ObjectMapper().readTree(response.toString());
//            String split = jsonNode.get("similarities").toString();
//            Double cosineSim = Double.valueOf(split.substring(1, split.length()-1));
////            log.info("Generated Cosine Similarity: {}", cosineSim);
//            tweet.setSimScore(cosineSim);
        }

}
