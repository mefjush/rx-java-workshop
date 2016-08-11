package com.doggybites;

import rx.Observable;
import rx.subjects.PublishSubject;

public class AsyncTwitterService {

    private final PublishSubject<Tweet> publishSubject = PublishSubject.create();

    private final TwitterService twitterService;

    public AsyncTwitterService(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

    public Observable<Tweet> getTweets() {
        return publishSubject.asObservable();
    }

    public void start() {
        //TODO
    }

    public void stop() {
        //TODO
    }
}
