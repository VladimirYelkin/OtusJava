package ru.otus.repository;


import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.otus.domain.Training;

@Repository
public interface TrainingRepository extends ReactiveCrudRepository<Training, Long> {

    @Query("SELECT pt.id as id, idtype, datastart, idcoach, minstudy, maxstudy  FROM plannedtraining pt " +
            "where pt.idcoach = :coach_id")
    Flux<Training> findByIdCoach(@Param("studyId")Long coachId);

    @Query("SELECT pt.id as id, idtype, datastart, idcoach, minstudy, maxstudy  FROM plannedtraining pt " +
            " inner join studyontranining st on pt.id = st.idtraining where st.idstudy = :study_id")
    Flux<Training> findByIdStudy(@Param("studyId")Long studyId);

//    @Query("SELECT pt.id as id, idtype, datastart, idcoach, minstudy, maxstudy  FROM plannedtraining pt " +
//            " inner join studyontranining st on pt.id = st.idtraining where st.idstudy = :study_id")
//    Flux<Training> listOfTraining(@Param("studyId")Long studyId);
    @Query("SELECT id, idtype, datastart, idcoach, minstudy, maxstudy FROM plannedtraining " +
            "WHERE datastart between CURRENT_DATE AND (CURRENT_DATE + '5 day'::interval) order by datastart")
    Flux<Training> listOfTraining5Days();
}
