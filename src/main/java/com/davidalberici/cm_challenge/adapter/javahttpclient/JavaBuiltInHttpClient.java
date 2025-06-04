package com.davidalberici.cm_challenge.adapter.javahttpclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JavaBuiltInHttpClient implements com.davidalberici.cm_challenge.port.HttpClient {
    private final HttpClient client;

    public JavaBuiltInHttpClient() {
        this.client = HttpClient.newHttpClient();
    }

    @Override
    public String get(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        return sendRequest(request);
    }

    @Override
    public String post(String url, String body) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return sendRequest(request);
    }

    @Override
    public String delete(String url, String body) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(body))
                .build();

        return sendRequest(request);
    }

    private String sendRequest(HttpRequest request) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("HTTP request failed", e);
        }
    }
}