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

    public synchronized void start() {
        twitterService.start();
        twitterService.subscribe(this::onNext);
    }

    private synchronized void onNext(Tweet tweet) {
        publishSubject.onNext(tweet);
    }

    public synchronized void stop() {
        publishSubject.onCompleted();
        twitterService.stop();
    }
}
