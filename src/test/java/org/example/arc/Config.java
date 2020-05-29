package org.example.arc;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

    public Config() {
        daoList.add(".*DAO*.");
//        daoList.add("repository");
//        daoList.add("db");
//        daoList.add("data");


        clientLibrary.add(".*.Client$");
        clientLibrary.add(".*.RestTemplate$");
        clientLibrary.add(".*.HttpURLConnection$");

    }
}
