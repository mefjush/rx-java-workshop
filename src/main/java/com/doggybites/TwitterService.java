package com.doggybites;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class TwitterService {

    private ObjectMapper objectMapper = new ObjectMapper();
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private BasicClient hosebirdClient;
    private LinkedBlockingQueue<String> msgQueue;

    public void start(String ... terms) {
        msgQueue = new LinkedBlockingQueue<>(100000);

        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        hosebirdEndpoint.trackTerms(Arrays.asList(terms));

        Authentication hosebirdAuth = Settings.INSTANCE.getTwitter();

        ClientBuilder builder = new ClientBuilder()
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));

        hosebirdClient = builder.build();

        System.out.println("Connecting...");

        // Attempts to establish a connection.
        hosebirdClient.connect();

        System.out.println("Connected!");
    }

    public void stop() {
        hosebirdClient.stop();
    }

    public void subscribe(Consumer<Tweet> consumer) {
        executor.submit(() -> {
            while (!hosebirdClient.isDone()) {
                try {
                    String msg = msgQueue.take();
                    JsonNode jsonNode = objectMapper.readTree(msg);
                    Tweet tweet = new Tweet(jsonNode);
                    consumer.accept(tweet);
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
