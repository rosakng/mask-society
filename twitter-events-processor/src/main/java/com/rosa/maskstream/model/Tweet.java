package com.rosa.maskstream.model;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Tweet {
    String id;
    String createdAt;
    String userName;
    String location;
    String text;
    String cosineSimilarity;
}
