package com.doggybites;

import com.twitter.hbc.httpclient.auth.OAuth1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {

    public static final Settings INSTANCE = new Settings("config.properties");

    private Properties prop = new Properties();

    public Settings(String file) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file);
        if (inputStream != null) {
            try {
                prop.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("property file '" + file + "' not found in the classpath");
        }
    }

    public OAuth1 getTwitter() {
        String consumerKey = prop.getProperty("consumerKey");
        String consumerSecret = prop.getProperty("consumerSecret");
        String token = prop.getProperty("token");
        String tokenSecret = prop.getProperty("tokenSecret");
        return new OAuth1(consumerKey, consumerSecret, token, tokenSecret);
    }

    public String getWeather() {
        return prop.getProperty("weatherKey");
    }
}
