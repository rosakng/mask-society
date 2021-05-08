package com.rosa.maskstream.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Tweet {
    String id;
    String createdAt;
    String userName;
    String location;
    String text;
    Double cosineSimilarity = -1.0;
}
