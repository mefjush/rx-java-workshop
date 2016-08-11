package com.doggybites;

import org.junit.Test;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BasicApiTest {

    @Test
    public void testTwitterApi() throws InterruptedException, IOException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        TwitterService twitterService = new TwitterService();

        twitterService.start("adwords", "google");

        twitterService.subscribe(tweet -> {
            System.out.println(tweet);
            assertNotNull(tweet);
            countDownLatch.countDown();
        });

        countDownLatch.await();
        twitterService.stop();
    }

    @Test
    public void testWeatherApi() {
        WeatherService weatherService = new WeatherService();

        Optional<Float> brussels = weatherService.getTemp("Brussels");
        Optional<Float> wroclaw = weatherService.getTemp("Wroclaw");

        System.out.println(brussels);
        System.out.println(wroclaw);

        assertTrue(brussels.isPresent());
        assertTrue(wroclaw.isPresent());
    }

    @Test
    public void testAdheseVersionApi() {
        AdheseVersionService adheseVersionService = new AdheseVersionService();

        String nrc = adheseVersionService.getCustomerVersion("nrc");
        String latest = adheseVersionService.getLatestVersion();

        System.out.println(nrc);
        System.out.println(latest);

        assertNotNull(nrc);
        assertNotNull(latest);
    }
}
