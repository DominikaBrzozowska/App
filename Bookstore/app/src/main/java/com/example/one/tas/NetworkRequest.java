package com.example.one.tas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkRequest {

    private final String url;
    private final HttpMethod method;
    private final String body;

    public NetworkRequest(String url, HttpMethod method, String body) {
        this.url = url;
        this.method = method;
        this.body = body;
    }

    public NetworkRequest(String url, HttpMethod method) {
        this(url, method, null);
    }

    public String execute() throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(method.getMethod());
            conn.setDoInput(true);

            if (body != null) {
                conn.getOutputStream().write(body.getBytes());
            }

            conn.connect();
            is = conn.getInputStream();

            return readStream(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readStream(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }
}
