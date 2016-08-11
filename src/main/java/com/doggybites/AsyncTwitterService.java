package com.doggybites;

import rx.Observable;

public class AsyncTwitterService {

    private final TwitterService twitterService = new TwitterService();

    Observable<Tweet> subscribe(String ... terms) {
        return Observable.error(new RuntimeException("Not implemented!"));
    }
}
