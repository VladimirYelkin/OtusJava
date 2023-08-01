package ru.otus.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.domain.StringValue;
import ru.otus.domain.StudyOnTraining;
import ru.otus.service.CoachStore;
import ru.otus.service.StudyStore;
import ru.otus.service.TrainingStory;


//  openAPI http://localhost:8888/swagger-ui.html

@Tag( name = "API-datastore", description = "api of datastore service")
@RequestMapping("/v1")
@RestController
public class DataController {
    private static final Logger log = LoggerFactory.getLogger(DataController.class);
    private final Scheduler workerPool;
    private final StudyStore studyStore;
    private final CoachStore coachStore;
    private final TrainingStory trainingStory;

    public DataController(StudyStore studyStore, CoachStore coachStore, Scheduler workerPool, TrainingStory trainingStory) {
        this.workerPool = workerPool;
        this.studyStore = studyStore;
        this.coachStore = coachStore;
        this.trainingStory = trainingStory;
    }



    @Operation(summary = "Get a Study by TelegramUid", description = "Return Study with TelegramUID")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
//    })
    @GetMapping(value = "/Study/{telegramUID}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<StringValue> getClientByUID(@Parameter(description = "telegram UID") @PathVariable("telegramUID") String telegramUID) {
        return Mono.just(telegramUID)
                .doOnNext(telegramUid -> log.info("getStudyByTelegramUID=:{}", telegramUid))
                .flatMapMany(studyStore::loadStudy)
                .log()
                .map(study -> new StringValue(study.toString(),study.getId()))
                .doOnNext(stringValue -> log.info("StringValue:{}", stringValue))
                .subscribeOn(workerPool);
    }

    @GetMapping(value = "/idStudy/{telegramUID}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<Long> getStudyIdByTelegramUID (@Parameter(description = "telegram UID") @PathVariable("telegramUID") String telegramUID) {
        return Mono.just(telegramUID)
                .doOnNext(telegramUid -> log.info("getStudyByTelegramUID=:{}", telegramUid))
                .flatMap(studyStore::getIdByTelegramUid)
                .log()
//                .map(study -> new StringValue(study.toString()))
                .doOnNext(stringValue -> log.info("StringValue:{}", stringValue))
                .subscribeOn(workerPool);
    }


    @Operation(summary = "Get a Coach by TelegramUid", description = "Return Coach with TelegramUID")
    @GetMapping(value = "/Coach/{telegramUID}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<StringValue> getCoachByUID(@PathVariable("telegramUID") String telegramUID) {
        return Mono.just(telegramUID)
                .doOnNext(telegramUid -> log.info("getCoachByTelegramUID=:{}", telegramUid))
                .flatMapMany(coachStore::loadCoach)
                .log()
                .map(study -> new StringValue(study.toString(),study.getId()))
                .doOnNext(stringValue -> log.info("StringValue:{}", stringValue))
                .subscribeOn(workerPool);
    }

    @Operation(summary = "Get a Training by ID", description = "Return Training with ID")
    @GetMapping(value = "/Training/{id}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<StringValue> getTrainingById(@PathVariable("id") Long id) {
        return Mono.just(id)
                .doOnNext(idx -> log.info("getTrainingById:{}", idx))
                .flatMap(trainingStory::loadTraining)
                .log()
                .map(training -> new StringValue(training.toString(), training.getId()))
                .doOnNext(stringValue -> log.info("StringValue:{}", stringValue))
                .subscribeOn(workerPool);
    }

    @Operation(summary = "Get a list of trainings for next days", description = "Return Trainings ")
    @GetMapping(value = "/listTrainings", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<StringValue> getTrainingsForNextDays() {
        return Mono.just(5L)
                .doOnNext(idx -> log.info("getTrainingById:{}", idx))
                .flatMapMany(aLong -> {return trainingStory.loadListTrainingFor5NextDays();})
                .log()
                .map(training -> new StringValue(training.toString(), training.getId()))
                .doOnNext(stringValue -> log.info("StringValue:{}", stringValue))
                .subscribeOn(workerPool);
    }


    @Operation(summary = "Get a Trainings by IdCoach", description = "Return Trainings with ID_Coach")
    @GetMapping(value = "/MyTrainingCoach/{idCoach}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<StringValue> getTrainingByIdCoach(@PathVariable("idCoach") Long id) {
        return Mono.just(id)
                .doOnNext(idx -> log.info("getTrainingById:{}", idx))
                .flatMapMany(trainingStory::loadTrainingByIdCoach)
                .log()
                .map(training -> new StringValue(training.toString(), training.getId()))
                .doOnNext(stringValue -> log.info("StringValue:{}", stringValue))
                .subscribeOn(workerPool);
    }

    @Operation(summary = "Get a Trainings by IdStudy", description = "Return Trainings with ID_Study")
    @GetMapping(value = "/MyTraining/{idStudy}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<StringValue> getTrainingByIdStudy(@PathVariable("idStudy") Long id) {
        return Mono.just(id)
                .doOnNext(idx -> log.info("getTrainingById:{}", idx))
                .flatMapMany(trainingStory::loadTrainingByIdStudy)
                .log()
                .map(training -> new StringValue(training.toString(), training.getId()))
                .doOnNext(stringValue -> log.info("StringValue:{}", stringValue))
                .subscribeOn(workerPool);
    }

    @Operation(summary = "Get Types Of Trainings", description = "Return types Of Tranings")
    @GetMapping(value = "/listTypeTraining", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<StringValue> getTypesOfTraining() {
        return   trainingStory.loadTypesOfTrainings()
                .map(typesOfTraining -> new StringValue(typesOfTraining.toString(),typesOfTraining.getId()))
                .doOnNext(stringValue -> log.info("StringValue:{}", stringValue))
                .subscribeOn(workerPool);
    }

    @Operation(summary = "Save a study on trainig", description = "save study {id} on  training{id} , and return id of record ")
    @PostMapping(value = "/signtraining/{idTraining}/{idStudy}", produces = MediaType.APPLICATION_NDJSON_VALUE )
    public Mono<Long> saveSignOnTraining(@PathVariable("idTraining") Long idTraining,
                                        @PathVariable("idStudy") Long idStudy) {

        return  Mono.just(new StudyOnTraining(idTraining, idStudy))
                .log()
                .doOnNext(studyOnTraining -> log.info("studyOnTraining:{}", studyOnTraining))
                .flatMap(studyOnTraining -> trainingStory.saveSignOnTraining(studyOnTraining))
                .publishOn(workerPool)
                .doOnNext(studyOnTraining -> log.info("saved id:{}", studyOnTraining.getId()))
                .map(StudyOnTraining::getId)
//                .map(id -> new StringValue(id.toString()))
                .subscribeOn(workerPool);

//        return recordStudyOnTraining;
    }


}
