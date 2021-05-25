package com.rosa.maskstream.support;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final Double SIM_SCORE_BUCKET_1 = 0.05;
    public static final Double SIM_SCORE_BUCKET_2 = 0.1;
    public static final Double SIM_SCORE_BUCKET_3 = 0.15;
    public static final Double SIM_SCORE_BUCKET_4 = 0.2;
    public static final Double SIM_SCORE_BUCKET_5 = 0.25;
    public static final Double SIM_SCORE_BUCKET_6 = 0.3;
    public static final Double SIM_SCORE_BUCKET_7 = 0.35;
    public static final Double SIM_SCORE_BUCKET_8 = 0.4;
    public static final Double SIM_SCORE_BUCKET_9 = 0.45;
    public static final Double SIM_SCORE_BUCKET_10 = 0.5;
    public static final Double SIM_SCORE_BUCKET_11 = 0.55;
    public static final Double SIM_SCORE_BUCKET_12 = 0.6;
    public static final Double SIM_SCORE_BUCKET_13 = 0.65;
    public static final Double SIM_SCORE_BUCKET_14 = 0.7;
    public static final Double SIM_SCORE_BUCKET_15 = 0.75;
    public static final Double SIM_SCORE_BUCKET_16 = 0.8;
    public static final Double SIM_SCORE_BUCKET_17 = 0.85;
    public static final Double SIM_SCORE_BUCKET_18 = 0.9;
    public static final Double SIM_SCORE_BUCKET_19 = 0.95;
    public static final Double SIM_SCORE_BUCKET_20 = 1.0;

    public static final String TWEET_KEYSPACE_NAME = "maskstream";
    public static final String TWEET_TABLE_NAME = "tweets";

    public static final String REPLICATION_STRATEGY = "SimpleStrategy";
    public static final int REPLICATION_FACTOR = 1;

    //locations that im interested in :]
    public static final List<String> LOCATIONS = Arrays.asList(
            "ontario",
            "alberta",
            "british columbia",
            "california",
            "texas",
            "new york",
            "seattle",
            "south carolina",
            "michigan",
            "ohio",
            "florida",
            "arizona",
            "boston",
            "new jersey",
            "utah",
            "alabama",
            "georgia",
            "hawaii",
            "massachusetts",
            "north carolina",
            "missouri",
            "louisiana",
            "connecticut"
    );

    public static final String ANCHOR = "masks are taking away freedom";

}
