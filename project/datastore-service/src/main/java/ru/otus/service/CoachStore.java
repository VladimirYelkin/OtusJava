package ru.otus.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.Coach;

public interface CoachStore {

    Mono<Coach> saveCoach(Coach study);

    Flux<Coach> loadCoach(String telegramUid);
}
