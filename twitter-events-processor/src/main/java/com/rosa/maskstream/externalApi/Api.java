package com.rosa.maskstream.externalApi;

import com.rosa.maskstream.model.Tweet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.CosineSimilarity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.rosa.maskstream.support.Constants.*;

@SuppressWarnings("unchecked")
@Slf4j
public class Api implements Serializable {

    public static void processCosineSimIntoBuckets(Tweet tweet) {
        String s1 = tweet.getTweetText();

        Map<CharSequence, Integer> vector1 = new HashMap<>();
        Map<CharSequence, Integer> vector2 = new HashMap<>();

        for (String token : s1.split(" ")) {
            vector1.put(token, vector1.getOrDefault(token, 0) + 1);
        }

        for (String token : ANCHOR.split(" ")) {
            vector2.put(token, vector2.getOrDefault(token, 0) + 1);
        }
        CosineSimilarity cosine = new CosineSimilarity();
        Double cosineSim = cosine.cosineSimilarity(vector1, vector2);

        Optional<String> bucket = getBucketString(cosineSim);
        tweet.setCosSimBucket(bucket.get());
        log.info("FINALIZED TWEET: {}", tweet.toString());
    }

    private static Optional<String> getBucketString(Double cosineSim) {
        Optional<String> bucket;
        if (cosineSim <= SIM_SCORE_BUCKET_1) {
            bucket = Optional.of("sim<=" + SIM_SCORE_BUCKET_1);
        } else if (cosineSim > SIM_SCORE_BUCKET_1 && cosineSim <= SIM_SCORE_BUCKET_2) {
            bucket = Optional.of(SIM_SCORE_BUCKET_1 + "<sim<=" + SIM_SCORE_BUCKET_2);
        } else if (cosineSim > SIM_SCORE_BUCKET_2 && cosineSim <= SIM_SCORE_BUCKET_3) {
            bucket = Optional.of(SIM_SCORE_BUCKET_2 + "<sim<=" + SIM_SCORE_BUCKET_3);
        } else if (cosineSim > SIM_SCORE_BUCKET_3 && cosineSim <= SIM_SCORE_BUCKET_4) {
            bucket = Optional.of(SIM_SCORE_BUCKET_3 + "<sim<=" + SIM_SCORE_BUCKET_4);
        } else if (cosineSim > SIM_SCORE_BUCKET_4 && cosineSim <= SIM_SCORE_BUCKET_5) {
            bucket = Optional.of(SIM_SCORE_BUCKET_4 + "<sim<=" + SIM_SCORE_BUCKET_5);
        } else if (cosineSim > SIM_SCORE_BUCKET_5 && cosineSim <= SIM_SCORE_BUCKET_6) {
            bucket = Optional.of(SIM_SCORE_BUCKET_5 + "<sim<=" + SIM_SCORE_BUCKET_6);
        } else if (cosineSim > SIM_SCORE_BUCKET_6 && cosineSim <= SIM_SCORE_BUCKET_7) {
            bucket = Optional.of(SIM_SCORE_BUCKET_6 + "<sim<=" + SIM_SCORE_BUCKET_7);
        } else if (cosineSim > SIM_SCORE_BUCKET_7 && cosineSim <= SIM_SCORE_BUCKET_8) {
            bucket = Optional.of(SIM_SCORE_BUCKET_7 + "<sim<=" + SIM_SCORE_BUCKET_8);
        } else if (cosineSim > SIM_SCORE_BUCKET_8 && cosineSim <= SIM_SCORE_BUCKET_9) {
            bucket = Optional.of(SIM_SCORE_BUCKET_8 + "<sim<=" + SIM_SCORE_BUCKET_9);
        } else if (cosineSim > SIM_SCORE_BUCKET_9 && cosineSim <= SIM_SCORE_BUCKET_10) {
            bucket = Optional.of(SIM_SCORE_BUCKET_9 + "<sim<=" + SIM_SCORE_BUCKET_10);
        } else if (cosineSim > SIM_SCORE_BUCKET_10 && cosineSim <= SIM_SCORE_BUCKET_11) {
            bucket = Optional.of(SIM_SCORE_BUCKET_10 + "<sim<=" + SIM_SCORE_BUCKET_11);
        } else if (cosineSim > SIM_SCORE_BUCKET_12 && cosineSim <= SIM_SCORE_BUCKET_13) {
            bucket = Optional.of(SIM_SCORE_BUCKET_12 + "<sim<=" + SIM_SCORE_BUCKET_13);
        } else if (cosineSim > SIM_SCORE_BUCKET_13 && cosineSim <= SIM_SCORE_BUCKET_14) {
            bucket = Optional.of(SIM_SCORE_BUCKET_13 + "<sim<=" + SIM_SCORE_BUCKET_14);
        } else if (cosineSim > SIM_SCORE_BUCKET_14 && cosineSim <= SIM_SCORE_BUCKET_15) {
            bucket = Optional.of(SIM_SCORE_BUCKET_14 + "<sim<=" + SIM_SCORE_BUCKET_15);
        } else if (cosineSim > SIM_SCORE_BUCKET_15 && cosineSim <= SIM_SCORE_BUCKET_16) {
            bucket = Optional.of(SIM_SCORE_BUCKET_15 + "<sim<=" + SIM_SCORE_BUCKET_16);
        } else if (cosineSim > SIM_SCORE_BUCKET_16 && cosineSim <= SIM_SCORE_BUCKET_17) {
            bucket = Optional.of(SIM_SCORE_BUCKET_16 + "<sim<=" + SIM_SCORE_BUCKET_17);
        } else if (cosineSim > SIM_SCORE_BUCKET_17 && cosineSim <= SIM_SCORE_BUCKET_18) {
            bucket = Optional.of(SIM_SCORE_BUCKET_17 + "<sim<=" + SIM_SCORE_BUCKET_18);
        } else if (cosineSim > SIM_SCORE_BUCKET_18 && cosineSim <= SIM_SCORE_BUCKET_19) {
            bucket = Optional.of(SIM_SCORE_BUCKET_18 + "<sim<=" + SIM_SCORE_BUCKET_19);
        } else if (cosineSim > SIM_SCORE_BUCKET_19 && cosineSim <= SIM_SCORE_BUCKET_20) {
            bucket = Optional.of(SIM_SCORE_BUCKET_19 + "<sim<=" + SIM_SCORE_BUCKET_20);
        } else {
            bucket = Optional.of("sim>" + SIM_SCORE_BUCKET_20);
        }
        return bucket;
    }
}
