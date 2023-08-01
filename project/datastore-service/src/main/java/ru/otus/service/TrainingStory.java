package ru.otus.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.StudyOnTraining;
import ru.otus.domain.Training;
import ru.otus.domain.TypeOfTraining;


public interface TrainingStory {

     Flux<Training> loadTrainingByIdStudy(Long idStudy);

     Flux<Training> loadTrainingByIdCoach(Long idCoach);


    Flux<Training> loadListTrainingFor5NextDays();

    Mono<Training> loadTraining(Long id);

    Mono<StudyOnTraining> saveSignOnTraining(StudyOnTraining studyOnTraining);

    Flux<TypeOfTraining> loadTypesOfTrainings();
}
