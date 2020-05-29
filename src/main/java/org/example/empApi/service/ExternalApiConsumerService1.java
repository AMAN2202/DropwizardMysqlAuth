package org.example.empApi.service;

import com.mysql.cj.xdevapi.JsonArray;
import lombok.NoArgsConstructor;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

@NoArgsConstructor
public class ExternalApiConsumerService1 {
    private final Client client = ClientBuilder.newClient();

    public void fun() {

        WebTarget target = client.target("http://host:8080/context/rest/method");
        JsonArray response = target.request(MediaType.APPLICATION_JSON).get(JsonArray.class);
    }

}
