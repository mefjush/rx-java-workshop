package com.doggybites;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class Step_05_Twitter {

    private TwitterService twitterService = new TwitterService("google", "microsoft");
    private AsyncTwitterService asyncTwitterService = new AsyncTwitterService(twitterService);
    private AsyncWeatherService asyncWeatherService = new AsyncWeatherService();

    @Before
    public void setUp() throws Exception {
        asyncTwitterService.start();
    }

    @After
    public void tearDown() throws Exception {
        asyncTwitterService.stop();
    }

    @Test(timeout = 3000)
    public void can_subscribe_to_tweets() {
        Observable<Tweet> tweets = asyncTwitterService.getTweets();

        Tweet first = tweets.take(1).toBlocking().first();

        assertNotNull(first);
    }

    @Test
    public void shows_3_hot_tweets() throws InterruptedException {
        AtomicInteger tempViolations = new AtomicInteger(0);
        CountDownLatch completed = new CountDownLatch(1);
        Observable<Tweet> tweets = asyncTwitterService.getTweets();

        Observable<ExtendedTweet> hotTweets = tweets
                .filter(tweet -> tweet.getUserLocation().isPresent())
                .flatMap(tweet -> {
                    String location = tweet.getUserLocation().get();
                    return asyncWeatherService.getTemp(location).map(temp -> new ExtendedTweet(tweet, temp));
                })
                .filter(tweet -> tweet.temp > 25)
                .onErrorResumeNext(Observable.empty());

        hotTweets.subscribe(
                tweet -> {
                    System.out.println(tweet);
                    if (tweet.temp < 25) {
                        tempViolations.incrementAndGet();
                    }
                },
                Throwable::printStackTrace,
                completed::countDown
        );
        completed.await();
        assertThat(tempViolations.get(), is(0));
    }

    @Test
    public void prints_all_the_tweets_for_5_seconds() throws InterruptedException, ExecutionException {
        AtomicInteger deadlineViolations = new AtomicInteger(0);
        LocalDateTime deadline = LocalDateTime.now().plusSeconds(5);
        Observable<Tweet> tweets = asyncTwitterService.getTweets();

        Subscription subscription = tweets.subscribe(
                tweet -> {
                    if (LocalDateTime.now().isAfter(deadline)) {
                        System.out.println("Booo after the deadline!");
                        deadlineViolations.incrementAndGet();
                    } else {
                        System.out.println(tweet);
                    }
                }
        );

        //TODO
        Observable.timer(5, TimeUnit.SECONDS).subscribe(timeout -> subscription.unsubscribe());

        Thread.sleep(10000);
        assertThat(deadlineViolations.get(), is(0));
    }
}
