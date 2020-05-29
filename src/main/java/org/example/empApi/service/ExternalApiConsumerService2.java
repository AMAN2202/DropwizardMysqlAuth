package org.example.empApi.service;

import lombok.NoArgsConstructor;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;

@NoArgsConstructor
public class ExternalApiConsumerService2 {
    RestTemplate restTemplate = new RestTemplate();


    public void fun() {

        Response response = restTemplate.getForObject(
                "http://services.groupkt.com/country/get/iso2code/IN",
                Response.class);
    }

}
