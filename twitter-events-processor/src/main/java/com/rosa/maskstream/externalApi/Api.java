package com.rosa.maskstream.externalApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rosa.maskstream.config.ApiProperties;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@SuppressWarnings("unchecked")
@Slf4j
public class Api {

    private final ApiProperties apiProperties;

    public Api (ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    public Double getSimilarityScore(String text) throws IOException {
        URL url = new URL(apiProperties.getUrl());

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Bearer: " + apiProperties.getToken());
        con.setDoOutput(true);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("anchor", apiProperties.getAnchor());
        jsonObject.put("targets", Collections.singletonList(text));


        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            JsonNode jsonNode = new ObjectMapper().readTree(response.toString());
            String split = jsonNode.get("similarities").toString();
            Double cosineSim = Double.valueOf(split.substring(1, split.length()-1));
            log.info("Generated Cosine Similarity: {}", cosineSim);
            return cosineSim;
        }
    }
}
