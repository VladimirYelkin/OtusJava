package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.domain.Study;
import ru.otus.repository.StudyRepository;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public class StudyStoreR2dbc implements StudyStore {
    private static final Logger log = LoggerFactory.getLogger(StudyStoreR2dbc.class);
    private final StudyRepository studyRepository;
    private final Scheduler workerPool;

    public StudyStoreR2dbc(Scheduler workerPool, StudyRepository studyRepository) {
        this.workerPool = workerPool;
        this.studyRepository = studyRepository;
    }

    @Override
    public Mono<Study> saveStudy(Study study) {
        log.info("saveStudy:{}", study);
        return studyRepository.save(study);
    }

    @Override
    public Flux<Study> loadStudy (String telegramUid) {

//        existsStudy(telegramUid).log().subscribe();
        log.info("loadStudyByTelegramUid {}", telegramUid);
        return studyRepository.findByTelegramUid(telegramUid)
                .delayElements(Duration.of(1, SECONDS), workerPool);
    }

//    @Override
//    public Mono<Void> existsStudy (String telegramUid) {
//        log.info("existsStudyByTelegramUid {}", telegramUid);
//        return studyRepository.existsByTelegramUid(telegramUid);
//    }

}
