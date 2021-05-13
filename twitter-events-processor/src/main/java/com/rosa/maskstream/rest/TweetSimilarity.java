package com.rosa.maskstream.rest;

import com.datastax.driver.mapping.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Table(name = "")
public class TweetSimilarity {
    private String location_similarity;
    private int num_occurrences;
}
