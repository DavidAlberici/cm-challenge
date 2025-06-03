package com.davidalberici.cm_challenge.cmadapter;

public interface HttpClient {
    String get(String url);
    String post(String url, String body);
    String delete(String url, String body);
}
