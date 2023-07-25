package ru.otus.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.Study;

public interface StudyRepository extends ReactiveCrudRepository<Study, Long> {

    @Query("select * from study where telegram_uid = :telegram_uid order by id")
    Flux<Study> findByTelegramUid(@Param("telegramUid") String telegramUid);


//    @Query("SELECT exists  (SELECT id  FROM public.study where telegram_uid = :telegram_uid )")
//    Mono<Void> existsByTelegramUid(@Param("telegramUid") String telegramUid);

}
