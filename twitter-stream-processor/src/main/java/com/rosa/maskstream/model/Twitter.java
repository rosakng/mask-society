package com.rosa.maskstream.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Twitter {
    private final long id;
    private final String text;
    private final String createdAt;
    private final String lang;
    private final String timestampMS;
}
