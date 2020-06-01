package org.example.arc;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*
use regex for all operation
 */
@Getter
public class Config {

    /*
    DAO List containg regex expression for DAOs
     */
    List<String> daoList = new ArrayList<>();
    /*
    Libraray class name beign used for aceesing APi like RestTemplate, Client,HttpURLConnection
    Can also conatin regex
     */
    List<String> clientLibrary = new ArrayList<>();

    /*
    List of class that you want to excldue for checks
     */

    List<String> exclude = new ArrayList<>();

    /*
    List of metrics annotation to check
     */
    List<String> metricsAnnotations = new ArrayList<>();

    /*
    List of annotation for recognising controllers
     */
    List<String> controllerRequestAnnotations = new ArrayList<>();

    List<String> packagesToCheckCycle = new ArrayList<>();


    Map<String, List<String>> layers = new TreeMap<>();
    Map<String, List<String>> acessRules = new TreeMap<>();
    Map<String,String> ignoreClasses =new TreeMap<>();

    public Config() {
        daoList.add(".*DAO*.");
//        daoList.add("repository");
//        daoList.add("db");
//        daoList.add("data");


        clientLibrary.add(".*.Client$");
        clientLibrary.add(".*.RestTemplate$");
        clientLibrary.add(".*.HttpURLConnection$");

        exclude.add(".*.ExternalApiConsumerService[1-2]");

        metricsAnnotations.add("com.codahale.metrics.annotation.Timed");
        metricsAnnotations.add("com.codahale.metrics.annotation.Metered");
        metricsAnnotations.add("com.codahale.metrics.annotation.ResponseMetered");
        metricsAnnotations.add("com.codahale.metrics.annotation.ExceptionMetered");


        controllerRequestAnnotations.add("javax.ws.rs.GET");
        controllerRequestAnnotations.add("javax.ws.rs.POST");
        controllerRequestAnnotations.add("javax.ws.rs.DELETE");
        controllerRequestAnnotations.add("javax.ws.rs.PUT");


        packagesToCheckCycle.add("org.example.empApi.(*)..");

        layers.put("Controllers", List.of("org.example.empApi.resources.."));
        layers.put("Services", List.of("org.example.empApi.service.."));
        layers.put("Persistence",List.of("org.example.empApi.db.."));


        acessRules.put("Services", List.of("Controllers"));
        acessRules.put("Persistence",List.of("Services"));
        acessRules.put("Controllers", List.of());

        ignoreClasses.put("org.example.empApi.service.ExternalApiConsumerService1","org.example.empApi.db.EmployeeDAOImpl");



    }
}
