package ru.otus.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.Coach;

@Repository
public interface CoachRepository extends ReactiveCrudRepository<Coach, Long> {

    @Query("select * from coach where telegram_uid = :telegram_uid order by id")
    Flux<Coach> findByTelegramUid(@Param("telegramUid") String telegramUid);

    @Query("SELECT exists  (SELECT id  FROM coach where telegram_uid = :telegram_uid )")
    Mono<Boolean> existsByTelegramUid(@Param("telegramUid") String telegramUid);

    @Query("SELECT id  FROM coach where telegram_uid = :telegram_uid )")
    Mono<Long> getIdByTelegramUid(@Param("telegramUid") String telegramUid);
}
