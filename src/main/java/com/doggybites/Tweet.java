package com.doggybites;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public class Tweet {

    private final JsonNode jsonNode;

    public Tweet(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    public String getText() {
        return getChildNode(jsonNode, "text").flatMap(this::jsonText).orElse("");
    }

    public Optional<String> getUserLocation() {
        return getUsers("location").flatMap(this::jsonText);
    }

    public String getUserName() {
        return getUsers("screen_name").flatMap(this::jsonText).orElse("");
    }

    public Integer getUserFollowersCount() {
        return getUsers("followers_count").flatMap(node -> Optional.ofNullable(node.intValue())).orElse(0);
    }

    private Optional<JsonNode> getUsers(String nodeName) {
        return getChildNode(jsonNode, "user")
                .flatMap(userNode -> getChildNode(userNode, nodeName));
    }

    private Optional<String> jsonText(JsonNode jsonNode) {
        return Optional.ofNullable(jsonNode.textValue());
    }

    private Optional<JsonNode> getChildNode(JsonNode jsonNode, String name) {
        return Optional.ofNullable(jsonNode.get(name));
    }

    @Override
    public String toString() {
        return String.format("user: %s\nlocation: %s\nfollowers: %s\ntext: %s", getUserName(), getUserLocation(), getUserFollowersCount(), getText());
    }
}
