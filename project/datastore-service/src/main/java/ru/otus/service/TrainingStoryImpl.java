package ru.otus.service;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.domain.StudyOnTraining;
import ru.otus.domain.Training;
import ru.otus.domain.TypeOfTraining;
import ru.otus.repository.CoachRepository;
import ru.otus.repository.StudyOnTrainingRepository;
import ru.otus.repository.TrainingRepository;
import ru.otus.repository.TypeOfTrainingRepository;

@Service
public class TrainingStoryImpl implements TrainingStory {
    private static final Logger log = LoggerFactory.getLogger(TrainingStoryImpl.class);
    private final TrainingRepository trainingRepository;
    private final TypeOfTrainingRepository typeOfTrainingRepository;
    private final Scheduler workerPool;
    private final CoachRepository coachRepository;
    private final StudyOnTrainingRepository studyOnTrainingRepository;

    public TrainingStoryImpl(TrainingRepository trainingRepository, TypeOfTrainingRepository typeOfTrainingRepository,
                             CoachRepository coachRepository, StudyOnTrainingRepository studyOnTrainingRepository,
                             Scheduler workerPool) {
        this.trainingRepository = trainingRepository;
        this.workerPool = workerPool;
        this.typeOfTrainingRepository = typeOfTrainingRepository;
        this.coachRepository = coachRepository;
        this.studyOnTrainingRepository = studyOnTrainingRepository;
    }


    @Override
    public Flux<Training> loadTrainingByIdStudy(Long idStudy) {
        log.info("listOfTrainingsByIdStudy {}", idStudy);
        return trainingRepository.findByIdStudy(idStudy).log().publishOn(workerPool)
                .flatMap(this::addedTypeTrainingInfo)
                .subscribeOn(workerPool)
                .flatMap(this::addedCoachInfo);
    }

    @Override
    public Flux<Training> loadTrainingByIdCoach(Long idCoach) {
        log.info("listOfTrainingsByIdCoach {}", idCoach);
        return trainingRepository.findByIdCoach(idCoach).log().publishOn(workerPool)
                .flatMap(this::addedTypeTrainingInfo)
                .subscribeOn(workerPool)
                .flatMap(this::addedCoachInfo);
    }

    @Override
    public Flux<Training> loadListTrainingFor5NextDays() {
        log.info("loadListTrainingFor5NextDays ");
        return trainingRepository.listOfTraining5Days()
                .log()
                .publishOn(workerPool)
                .flatMap(this::addedTypeTrainingInfo)
                .subscribeOn(workerPool)
                .flatMap(this::addedCoachInfo);
    }

    @Override
    public Mono<Training> loadTraining(Long id) {
        log.info("trainingById {}", id);

        return trainingRepository.findById(id)
                .log()
                .publishOn(workerPool)
                .flatMapMany(training -> addedTypeTrainingInfo(training))
                .flatMap(this::addedCoachInfo)
                .publishOn(workerPool)
                .elementAt(0);
    }



    @Override
    public Mono<StudyOnTraining> saveSignOnTraining(StudyOnTraining studyOnTraining) {
        log.info("saveSignOnTraining {}", studyOnTraining );
        return studyOnTrainingRepository
                .saveRecord(studyOnTraining.getIdTraining(), studyOnTraining.getIdStudy());
    }


    @Override
    public Flux<TypeOfTraining> loadTypesOfTrainings() {
        return typeOfTrainingRepository.findAll();
    }

    private Publisher<Training> addedCoachInfo(Training trainingPre) {
        if (trainingPre.getIdCoach() == null) {
            return Mono.just(trainingPre);
        }
        return coachRepository.findById(trainingPre.getIdCoach())
                .subscribeOn(workerPool)
                .log()
                .publishOn(workerPool)
                .map(coach -> preBuildTraining(trainingPre)
                        .coach(coach)
                        .build());
    }

    private Publisher<Training> addedTypeTrainingInfo(Training trainingPre) {
        if (trainingPre.getIdType() == null) {
            return Mono.just(trainingPre);
        }
        return typeOfTrainingRepository.findById(trainingPre.getIdType())
                .subscribeOn(workerPool)
                .log("addedTypeTrainingInfo")
                .publishOn(workerPool)
                .map(typeOfTraining -> preBuildTraining(trainingPre)
                        .typeOfTraining(typeOfTraining)
                        .build());
    }

    private  Training.TrainingBuilder preBuildTraining(Training training) {
        return Training.builder().id(training.getId()).idType(training.getIdType())
                .localDateTime(training.getLocalDateTime())
                .minColStudy(training.getMinColStudy())
                .maxColStudy(training.getMaxColStudy())
                .idCoach(training.getIdCoach())
                .coach(training.getCoach())
                .typeOfTraining(training.getTypeOfTraining());
    }
}
