package org.example.empApi.service;

import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@NoArgsConstructor
public class ExternalApiConsumerService3 {


    public void fun() throws IOException {
        URL url = new URL("http://localhost:8080/RESTfulExample/json/product/get");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

    }

}
