package com.rosa.maskstream.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User {
    private final long id;
    private final String name;
    private final String location;

    private String screenName;
}
