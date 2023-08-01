package ru.otus.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.Study;

@Repository
public interface StudyRepository extends ReactiveCrudRepository<Study, Long> {

    @Query("select * from study where telegram_uid = :telegram_uid order by id")
    Flux<Study> findByTelegramUid(@Param("telegramUid") String telegramUid);


    @Query("SELECT exists  (SELECT id  FROM public.study where telegram_uid = :telegram_uid )")
    Mono<Boolean> existsByTelegramUid(@Param("telegramUid") String telegramUid);

    @Query("SELECT id  FROM study where telegram_uid = :telegram_uid")
    Mono<Long> getIdByTelegramUid(@Param("telegramUid") String telegramUid);

}
