package com.doggybites;

import rx.Observable;
import rx.subjects.PublishSubject;

public class AsyncTwitterService {

    private final PublishSubject<Tweet> publishSubject = PublishSubject.create();

    private final TwitterService twitterService;

    public AsyncTwitterService(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

    Observable<Tweet> getTweets() {
        publishSubject.doOnSubscribe(this::subscribe);
        publishSubject.doOnUnsubscribe(this::unsubscribe);
        return publishSubject.asObservable();
    }

    private synchronized void subscribe() {
        //TODO
    }

    private synchronized void unsubscribe() {
        //TODO
    }
}
