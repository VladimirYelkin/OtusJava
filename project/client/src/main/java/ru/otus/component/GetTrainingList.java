package ru.otus.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Flux;
import ru.otus.StringValue;


public class GetTrainingList implements GetData {
    private static final Logger log = LoggerFactory.getLogger(GetTrainingList.class);


    @Override
    public Flux<StringValue> exec(WebClient datastoreClient, Message message) {
        var result = data(datastoreClient,message.getChat().getId());
        return result.log()
                .defaultIfEmpty(new StringValue("study with telegram %d not found".formatted(message.getChat().getId()),0L));
    }

    public Flux<StringValue> data(WebClient datastoreClient,  long seed) {
        log.info("request for data, seed:{}", seed);

        return datastoreClient.get().uri(String.format("/v1/listTrainings"))
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(StringValue.class)
                .map(stringValue -> {return new StringValue("/rec"+stringValue.id()+" "+stringValue.value(),stringValue.id());})
                .doOnNext(stringValue -> log.info("val:{}", stringValue));
    }
}
