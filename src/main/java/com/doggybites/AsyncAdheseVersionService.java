package com.doggybites;

import rx.Observable;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncAdheseVersionService {

    private AdheseVersionService adheseVersionService = new AdheseVersionService();
    private Executor executor = Executors.newFixedThreadPool(2);

    public Observable<String> getLatestVersion() {
        //TODO
        return Observable.create(subscriber -> {
            String latestVersion = adheseVersionService.getLatestVersion();
            subscriber.onNext(latestVersion);
            subscriber.onCompleted();
        });
    }

    public Observable<String> getCustomerVersion(String customer) {
        //TODO
        return Observable.create(subscriber -> {
            String latestVersion = adheseVersionService.getCustomerVersion(customer);
            subscriber.onNext(latestVersion);
            subscriber.onCompleted();
        });
    }
}
