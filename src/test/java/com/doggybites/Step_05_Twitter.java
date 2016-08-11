package com.doggybites;

import org.junit.Test;
import rx.Observable;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class Step_05_Twitter {

    private TwitterService twitterService = new TwitterService("google", "microsoft");
    private AsyncTwitterService asyncTwitterService = new AsyncTwitterService(twitterService);
    private AsyncWeatherService asyncWeatherService = new AsyncWeatherService();

    //show how to unsubscribe + free up the underlying resource
    //use rx.Subject
    //show tweets with tags "adwords", "google" from hot places only

    @Test
    public void can_subscribe_to_tweets() {
        Observable<Tweet> tweets = asyncTwitterService.getTweets();

        Tweet first = tweets.take(1).toBlocking().first();

        assertNotNull(first);
    }

    @Test(timeout = 6000)
    public void prints_all_the_tweets_for_5_seconds() {
        Observable<Tweet> tweets = asyncTwitterService.getTweets();

        //TODO
    }

    @Test(timeout = 1000)
    public void starts_twitter_connection_once() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        TwitterService twitterService = mock(TwitterService.class);
        AsyncTwitterService asyncTwitterService = new AsyncTwitterService(twitterService);
        Observable<Tweet> tweets = asyncTwitterService.getTweets();

        tweets.take(1).subscribe(System.out::println, System.err::println, countDownLatch::countDown);
        tweets.skip(1).take(1).subscribe(System.out::println, System.err::println, countDownLatch::countDown);

        countDownLatch.await();
        verify(twitterService, times(1)).start();
    }

    @Test(timeout = 1000)
    public void stops_twitter_connection_if_noone_subscribes() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        TwitterService twitterService = mock(TwitterService.class);
        AsyncTwitterService asyncTwitterService = new AsyncTwitterService(twitterService);
        Observable<Tweet> tweets = asyncTwitterService.getTweets();

        tweets.take(1).subscribe(System.out::println, System.err::println, countDownLatch::countDown);

        countDownLatch.await();
        verify(twitterService).stop();
    }

    @Test
    public void shows_3_hot_tweets() throws InterruptedException {
        CountDownLatch completed = new CountDownLatch(1);
        Observable<Tweet> tweets = asyncTwitterService.getTweets();

        Observable<ExtendedTweet> hotTweets = null; //TODO

        hotTweets.subscribe(
                tweet -> {
                    System.out.println(tweet);
                    assertTrue(tweet.temp > 25);
                },
                err -> fail(err.getMessage()),
                completed::countDown
        );
        completed.await();
    }
}
