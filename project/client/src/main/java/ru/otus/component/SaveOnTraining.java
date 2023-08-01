package ru.otus.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Flux;
import ru.otus.StringValue;


public class SaveOnTraining implements GetData {
    private static final Logger log = LoggerFactory.getLogger(SaveOnTraining.class);


    @Override
    public Flux<StringValue> exec(WebClient datastoreClient, Message message) {
        log.info("save on training {}",message.getText());
        var result = data(datastoreClient,message);
        return result.log()
                .defaultIfEmpty(new StringValue("study with telegram %d not found".formatted(message.getChat().getId()),0L));
    }

    public Flux<StringValue> data(WebClient datastoreClient, Message message) {
        var idTraining = message.getText().replace("/rec","");
        var telegramUid=message.getChat().getId();
        log.info("request for save: id training {}  TelegramUid ", idTraining, telegramUid);

        return datastoreClient.get().uri(String.format("/v1/idStudy/%d", telegramUid))
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToMono(Long.class)
                .doOnNext(aLong -> log.info("val ID:{}", aLong))
                .flatMapMany(idOfStudy -> {
                    return datastoreClient.post().uri(String.format("/v1/signtraining/%s/%d", idTraining,idOfStudy))
                            .accept(MediaType.APPLICATION_NDJSON)
                            .retrieve()
                            .bodyToFlux(Long.class)
                            .map(value -> {return new StringValue("your record is = "+value, value);});
                })
                .doOnNext(val -> log.info("val:{}", val));
    }
}
