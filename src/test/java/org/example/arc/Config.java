package org.example.arc;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
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
    List of class that you want to exclue for checks
     */

    List<String> exclude =new ArrayList<>();



    public Config() {
        daoList.add(".*DAO*.");
//        daoList.add("repository");
//        daoList.add("db");
//        daoList.add("data");


        clientLibrary.add(".*.Client$");
        clientLibrary.add(".*.RestTemplate$");
        clientLibrary.add(".*.HttpURLConnection$");

        exclude.add(".*.ExternalApiConsumerService[1-2]");
    }
}
