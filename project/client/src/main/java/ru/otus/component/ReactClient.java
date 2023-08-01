package ru.otus.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import ru.otus.StringValue;

@Component
public class ReactClient {
     private static final Logger log = LoggerFactory.getLogger(ReactClient.class);

     public WebClient datastoreClient;

     public ReactClient(WebClient datastoreClient) {
          this.datastoreClient = datastoreClient;
     }

     public Flux<StringValue> infoOfStudyTelegramUID(long telegramUID) {
          log.info("request for data, seed:{}", telegramUID);

          return datastoreClient.get().uri(String.format("/v1/Study/%d", telegramUID))
                  .accept(MediaType.APPLICATION_NDJSON)
                  .retrieve()
                  .bodyToFlux(StringValue.class)
                  .doOnNext(val -> log.info("val:{}", val));
     }
}
