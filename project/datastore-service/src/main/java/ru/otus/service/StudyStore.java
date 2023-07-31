package ru.otus.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.Study;

public interface StudyStore {

    Mono<Study> saveStudy(Study study);

    Flux<Study> loadStudy(String telegramUid);

    Mono<Boolean> existsStudy(String telegramUid);
}
