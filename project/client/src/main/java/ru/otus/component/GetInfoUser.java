package ru.otus.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Flux;
import ru.otus.StringValue;

@Component
public class GetInfoUser implements GetData {
    private static final Logger log = LoggerFactory.getLogger(GetInfoUser.class);

    @Override
    public Flux<StringValue> exec(WebClient datastoreClient, Message message) {
        var result = data(datastoreClient,message.getChat().getId());
        return result.log()
                .defaultIfEmpty(new StringValue("study with telegram %d not found".formatted(message.getChat().getId()),0L));
    }

    public Flux<StringValue> data(WebClient datastoreClient,  long telegramUID) {
        log.info("request for Info  for Telegram UID {}", telegramUID);

        return datastoreClient.get().uri(String.format("/v1/Study/%d", telegramUID))
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(StringValue.class)
                .doOnNext(val -> log.info("val:{}", val));
    }
}
