package com.rosa.maskstream.support;

import com.rosa.maskstream.consumer.TweetAggregatorCron;

public class CronTask {

    public static void main(String[] args) {
        TweetAggregatorCron tweetAggregatorCron = new TweetAggregatorCron();
        tweetAggregatorCron.execute();
    }
}
