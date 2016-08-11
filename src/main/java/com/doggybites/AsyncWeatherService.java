package com.doggybites;

import rx.Observable;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncWeatherService {

    private Executor executor = Executors.newFixedThreadPool(2);
    private WeatherService weatherService = new WeatherService();

    public Observable<Float> getTemp(String city) {
        return Observable.create(subscriber -> {
            executor.execute(() -> {
                Optional<Float> temp = weatherService.getTemp(city);
                if (temp.isPresent()) {
                    subscriber.onNext(temp.get());
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new RuntimeException("No temperature available!"));
                }
            });
        });
    }
}
