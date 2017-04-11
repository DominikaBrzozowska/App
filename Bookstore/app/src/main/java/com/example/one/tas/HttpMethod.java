package com.example.one.tas;

public enum HttpMethod {
    GET("GET"), POST("POST"),    PUT("PUT");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
