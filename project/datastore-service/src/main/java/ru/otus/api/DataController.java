package ru.otus.api;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.domain.StringValue;
import ru.otus.service.CoachStore;
import ru.otus.service.StudyStore;

@RestController
public class DataController {
    private static final Logger log = LoggerFactory.getLogger(DataController.class);
    private final Scheduler workerPool;
    private final StudyStore studyStore;
    private final CoachStore coachStore;

    public DataController(StudyStore studyStore, CoachStore coachStore, Scheduler workerPool) {
        this.workerPool = workerPool;
        this.studyStore = studyStore;
        this.coachStore = coachStore;
    }

    @GetMapping(value = "/Study/{telegramUID}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<StringValue> getClientByUID(@PathVariable("telegramUID") String telegramUID) {
        return Mono.just(telegramUID)
                .doOnNext(telegramUid -> log.info("getStudyByTelegramUID, room:{}", telegramUid))
                .flatMapMany(studyStore::loadStudy)
                .log()
                .map(study -> new StringValue(study.toString()))
                .doOnNext(stringValue -> log.info("StringValue:{}", stringValue))
                .subscribeOn(workerPool);
    }

    @GetMapping(value = "/Coach/{telegramUID}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<StringValue> getCoachByUID(@PathVariable("telegramUID") String telegramUID) {
        return Mono.just(telegramUID)
                .doOnNext(telegramUid -> log.info("getCoachByTelegramUID, room:{}", telegramUid))
                .flatMapMany(coachStore::loadCoach)
                .log()
                .map(study -> new StringValue(study.toString()))
                .doOnNext(stringValue -> log.info("StringValue:{}", stringValue))
                .subscribeOn(workerPool);
    }
}
