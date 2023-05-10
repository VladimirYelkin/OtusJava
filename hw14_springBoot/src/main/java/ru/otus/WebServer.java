package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
     docker run --rm --name pg-docker -e POSTGRES_PASSWORD=pwd -e POSTGRES_USER=usr -e POSTGRES_DB=demoDB -p 5430:5432 postgres:13
     start page   http://localhost:8080

     api:  http://localhost:8080/api/client/       get all clients
           http://localhost:8080/api/client/{id}   get id client

*/
@SpringBootApplication
public class WebServer {
    public static void main(String[] args) {
        SpringApplication.run(WebServer.class, args);
    }

}
