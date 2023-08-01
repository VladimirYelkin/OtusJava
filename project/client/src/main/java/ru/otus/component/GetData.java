package ru.otus.component;

import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Flux;
import ru.otus.StringValue;

public interface GetData {

    Flux<StringValue> exec(WebClient dataStore, Message message);
}
