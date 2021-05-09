package com.rosa.maskstream.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Tweet {
    UUID id;
    String createdAt;
    String username;
    String location;
    String tweetText;
    String simScore;
}
