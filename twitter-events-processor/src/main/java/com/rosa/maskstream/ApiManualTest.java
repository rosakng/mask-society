package com.rosa.maskstream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class ApiManualTest {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://api.cohere.ai/baseline-squid/similarity");
        String token = "xxx";

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Bearer: " + token);
        con.setDoOutput(true);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("anchor", "masks are taking away freedom");
        jsonObject.put("targets", Collections.singletonList("I will harrass anyone who wears a mask"));


        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            JsonNode jsonNode = new ObjectMapper().readTree(response.toString());
            String split = jsonNode.get("similarities").toString();
            System.out.println(Double.valueOf(split.substring(1, split.length() - 1)));
        }
    }
}
