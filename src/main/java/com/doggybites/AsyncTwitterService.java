package com.doggybites;

import rx.Observable;

public class AsyncTwitterService {

    private final TwitterService twitterService;

    public AsyncTwitterService(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

    Observable<Tweet> getTweets() {
        return Observable.error(new RuntimeException("Not implemented!"));
    }
}
