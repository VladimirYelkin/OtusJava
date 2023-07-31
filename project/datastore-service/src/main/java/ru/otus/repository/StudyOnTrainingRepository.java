package ru.otus.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.Coach;
import ru.otus.domain.StudyOnTraining;
import ru.otus.domain.Training;

public interface  StudyOnTrainingRepository extends ReactiveCrudRepository<StudyOnTraining, Long> {

      @Query("with s as (select id, idtraining, idstudy from studyontranining s where idtraining = :id_training  and idstudy = :id_study ), " +
              "i as ( insert into studyontranining (idtraining, idstudy) select :id_training, :id_study where not exists (select 1 from s) " +
              "returning id, idtraining, idstudy ) " +
              "select id, idtraining, idstudy from i union all select id, idtraining, idstudy from s")
//    @Query("select ")
      Mono<StudyOnTraining> saveRecord (@Param("id_training") Long idTraining,@Param("id_study") Long idStudy);

//    @Query("select * from coach where telegram_uid = :telegram_uid order by id")
//    Flux<StudyOnTraining> saveRecord(@Param("id_training") Long idTraining,@Param("id_study") Long idStudy);




//    @Query("select * from studyontranining where idtraining = :id_training and idstudy = :id_study order by id")
//    Mono<StudyOnTraining> loadRecordByIdTrainingIdStudy (@Param("id_training") Long idTraining,@Param("id_study") Long idStudy)

}
