package com.rosa.maskstream.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Tweet {
    UUID id;
    String createdAt;
    String username;
    String location;
    String tweetText;
    Double simScore = -1.0;
}
