package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.domain.Coach;
import ru.otus.repository.CoachRepository;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public class CoachStoreR2dbc implements CoachStore {
    private static final Logger log = LoggerFactory.getLogger(CoachStoreR2dbc.class);
    private final CoachRepository coachRepository;
    private final Scheduler workerPool;

    public CoachStoreR2dbc(Scheduler workerPool, CoachRepository coachRepository) {
        this.workerPool = workerPool;
        this.coachRepository = coachRepository;
    }

    @Override
    public Mono<Coach> saveCoach(Coach study) {
        log.info("saveCoach:{}", study);
        return coachRepository.save(study);
    }

    @Override
    public Flux<Coach> loadCoach (String telegramUid) {
        log.info("loadCoachByTelegramUid {}", telegramUid);
        return coachRepository.findByTelegramUid(telegramUid)
                .delayElements(Duration.of(1, SECONDS), workerPool);
    }
}
