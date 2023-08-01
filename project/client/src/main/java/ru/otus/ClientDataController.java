package ru.otus;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@RestController
public class ClientDataController {
    private static final Logger log = LoggerFactory.getLogger(ClientDataController.class);

    private final WebClient client;

    public ClientDataController(WebClient datastoreClient) {
        this.client = datastoreClient;
    }

}
