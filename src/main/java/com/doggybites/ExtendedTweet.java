package com.doggybites;

class ExtendedTweet {

    final Tweet tweet;
    final Float temp;

    ExtendedTweet(Tweet tweet, Float temp) {
        this.tweet = tweet;
        this.temp = temp;
    }

    @Override
    public String toString() {
        return tweet.toString() + "\ntemp: " + temp;
    }
}
