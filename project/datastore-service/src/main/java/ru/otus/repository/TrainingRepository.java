package ru.otus.repository;


import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.Training;

@Repository
public interface TrainingRepository extends ReactiveCrudRepository<Training, Long> {

    @Query("SELECT pt.id as id, idtype, datastart, idcoach, minstudy, maxstudy  FROM plannedtraining pt " +
            "where pt.idcoach = :coach_id")
    Flux<Training> findByIdCoach(@Param("studyId")Long coachId);

    @Query("SELECT pt.id as id, idtype, datastart, idcoach, minstudy, maxstudy  FROM plannedtraining pt " +
            " inner join studyontranining st on pt.id = st.idtraining where st.idstudy = :study_id")
    Flux<Training> findByIdStudy(@Param("studyId")Long studyId);

}
