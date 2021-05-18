package com.maskstream.locality.support;

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

    private static final String sim = "sim";

    public static List<String> SIM_SCORE_THRESHOLDS_LIST = Arrays.asList(
            sim+"<="+SIM_SCORE_BUCKET_1,
            SIM_SCORE_BUCKET_1+"<"+sim+"<="+SIM_SCORE_BUCKET_2,
            SIM_SCORE_BUCKET_2+"<"+sim+"<="+SIM_SCORE_BUCKET_3,
            SIM_SCORE_BUCKET_3+"<"+sim+"<="+SIM_SCORE_BUCKET_4,
            SIM_SCORE_BUCKET_4+"<"+sim+"<="+SIM_SCORE_BUCKET_5,
            SIM_SCORE_BUCKET_5+"<"+sim+"<="+SIM_SCORE_BUCKET_6,
            SIM_SCORE_BUCKET_6+"<"+sim+"<="+SIM_SCORE_BUCKET_7,
            SIM_SCORE_BUCKET_7+"<"+sim+"<="+SIM_SCORE_BUCKET_8,
            SIM_SCORE_BUCKET_8+"<"+sim+"<="+SIM_SCORE_BUCKET_9,
            SIM_SCORE_BUCKET_9+"<"+sim+"<="+SIM_SCORE_BUCKET_10,
            SIM_SCORE_BUCKET_10+"<"+sim+"<="+SIM_SCORE_BUCKET_11,
            SIM_SCORE_BUCKET_11+"<"+sim+"<="+SIM_SCORE_BUCKET_12,
            SIM_SCORE_BUCKET_12+"<"+sim+"<="+SIM_SCORE_BUCKET_13,
            SIM_SCORE_BUCKET_13+"<"+sim+"<="+SIM_SCORE_BUCKET_14,
            SIM_SCORE_BUCKET_14+"<"+sim+"<="+SIM_SCORE_BUCKET_15,
            SIM_SCORE_BUCKET_15+"<"+sim+"<="+SIM_SCORE_BUCKET_16,
            SIM_SCORE_BUCKET_16+"<"+sim+"<="+SIM_SCORE_BUCKET_17,
            SIM_SCORE_BUCKET_17+"<"+sim+"<="+SIM_SCORE_BUCKET_18,
            SIM_SCORE_BUCKET_18+"<"+sim+"<="+SIM_SCORE_BUCKET_19,
            SIM_SCORE_BUCKET_19+"<"+sim+"<="+SIM_SCORE_BUCKET_20
    );
}
