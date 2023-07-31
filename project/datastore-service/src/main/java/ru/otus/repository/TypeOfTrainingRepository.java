package ru.otus.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.domain.TypeOfTraining;

public interface TypeOfTrainingRepository extends ReactiveCrudRepository<TypeOfTraining, Long> {
}
