package ru.otus.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Flux;
import ru.otus.StringValue;


public class GetMyTrainingList implements GetData {
    private static final Logger log = LoggerFactory.getLogger(GetMyTrainingList.class);


    @Override
    public Flux<StringValue> exec(WebClient datastoreClient, Message message) {
        var result = data(datastoreClient, message.getChat().getId());
        return result.log()
                .defaultIfEmpty(new StringValue("not found info about list of trainings for  telegram %d not found".formatted(message.getChat().getId()), 0L));
    }

    public Flux<StringValue> data(WebClient datastoreClient, long telegramUID) {
        log.info("request for training for Telegram UID:{}", telegramUID);

        var coachList = datastoreClient.get().uri(String.format("/v1/Coach/%d", telegramUID))
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(StringValue.class)
                .flatMap(stringValue -> {
                    return datastoreClient.get().uri(String.format("/v1/MyTrainingCoach/%d", stringValue.id()))
                            .accept(MediaType.APPLICATION_NDJSON)
                            .retrieve()
                            .bodyToFlux(StringValue.class);
                });


        return datastoreClient.get().uri(String.format("/v1/idStudy/%d", telegramUID))
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToMono(Long.class)
                .doOnNext(aLong -> log.info("val ID:{}", aLong))
                .flatMapMany(idOfStudy -> {
                    return datastoreClient.get().uri(String.format("/v1/MyTraining/%d", idOfStudy))
                            .accept(MediaType.APPLICATION_NDJSON)
                            .retrieve()
                            .bodyToFlux(StringValue.class)
//                            .map(stringValue -> {return new StringValue(stringValue.value(), stringValue.id());})
                            ;
                })
                .doOnNext(val -> log.info("val:{}", val))
                .switchIfEmpty(coachList);
    }
}
