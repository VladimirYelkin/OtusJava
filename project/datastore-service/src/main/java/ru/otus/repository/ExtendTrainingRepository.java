package ru.otus.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.otus.domain.Training;

@Repository
public interface ExtendTrainingRepository extends TrainingRepository{

////    @Query("select * from study where telegram_uid = :telegram_uid order by id")
//    @Query("SELECT pt.id as id, pt.idtype as idtype, pt.datastart as datastart, tt.nameoftraining , tt.duration as duration  \n" +
//            "   FROM plannedtraining pt \n" +
//            "    inner join typetraining tt on  pt.idtype = tt.id;")
//    Flux<Training> findFullInfoTraining(@Param("id") Long id);
}
