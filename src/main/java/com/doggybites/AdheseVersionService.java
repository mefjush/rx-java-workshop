package com.doggybites;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.function.Function;

public class AdheseVersionService {

    private DefaultHttpClient defaultHttpClient = new DefaultHttpClient(new PoolingClientConnectionManager());
    private ObjectMapper objectMapper = new ObjectMapper();

    public String getLatestVersion() {
        ResponseHandler<String> responseHandler = getResponseHandler(node -> {
            JsonNode latest = node.get("latest");
            return latest.textValue();
        });

        return execute(responseHandler);
    }

    public String getCustomerVersion(String customer) {
        ResponseHandler<String> responseHandler = getResponseHandler(jsonNode -> {
            ArrayNode released = (ArrayNode) jsonNode.get("released");
            Iterator<JsonNode> elements = released.elements();
            while (elements.hasNext()) {
                JsonNode next = elements.next();
                JsonNode customerNode = next.get("customer");
                if (customerNode.textValue().equals(customer)) {
                    return next.get("version").textValue();
                }
            }
            return null;
        });
        return execute(responseHandler);
    }

    private ResponseHandler<String> getResponseHandler(Function<JsonNode, String> versionExtractor) {
        return response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                String s = EntityUtils.toString(entity);
                JsonNode jsonNode = objectMapper.readTree(s);
                return versionExtractor.apply(jsonNode);
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
    }

    private String execute(ResponseHandler<String> responseHandler) {
        try {
            HttpGet httpget = new HttpGet("http://versions.adhese.org/api/versions/current");
            return defaultHttpClient.execute(httpget, responseHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
