package ru.otus.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.StringValue;

import java.util.ArrayList;

@Component
public class GetEmpty implements GetData {
    private static final Logger log = LoggerFactory.getLogger(GetEmpty.class);

    @Override
    public Flux<StringValue> exec(WebClient datastoreClient, Message message) {
        return Flux.from(
                Mono.just(new StringValue("command nod found".formatted(message.getChat().getId()), 0L))
        );
    }


}
