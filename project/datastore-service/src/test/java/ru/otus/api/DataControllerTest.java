package ru.otus.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.otus.domain.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DataControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getTrainingByIdTest() {
        var seed = 1L;
        var dataLimit = 1;
        var timeOut = 2;
        var result = webTestClient
                .get().uri(String.format("/v1/Training/%s", seed))
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(StringValue.class)
                .getResponseBody()
                .take(dataLimit)
                .timeout(Duration.ofSeconds(timeOut))
                .collectList()
                .block();

        assertThat(result).hasSize(dataLimit);
        var type = new TypeOfTraining(1L, "ОФП", LocalTime.parse("01:00"));
        var coach = new Coach(1L, "55112804", "TrenerИмя111", "TrenerФамилия111");
        var training = new Training(1L, 1L, 1L, LocalDateTime.parse("2023-08-01T19:00"), 3L, 10L, type, coach);
        var trainingList = Collections.singletonList(new StringValue(training.toString(),1L));
        assertThat(result).isEqualTo(trainingList);
    }

    @Test
    void getClientByUIDTest() {
        var clientTelegramUID = "55168805";
        var dataLimit = 1;
        var timeOut = 2;
        var result = webTestClient
                .get().uri(String.format("/v1/Study/%s", clientTelegramUID))
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(StringValue.class)
                .getResponseBody()
                .take(dataLimit)
                .timeout(Duration.ofSeconds(timeOut))
                .collectList()
                .block();
        assertThat(result).hasSize(dataLimit);
        assertThat(result).isEqualTo(Collections.singletonList(
                new StringValue((new Study(2L, "55168805", "Имя2", "Фамилия2").toString()),2L)
        ));
    }

    @Test
    void getCoachByUIDTest() {
        var coachTelegramUID = "55112804";
        var dataLimit = 1;
        var timeOut = 2;
        var result = webTestClient
                .get().uri(String.format("/v1/Coach/%s", coachTelegramUID))
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(StringValue.class)
                .getResponseBody()
                .take(dataLimit)
                .timeout(Duration.ofSeconds(timeOut))
                .collectList()
                .block();
        assertThat(result).hasSize(dataLimit);
        assertThat(result).isEqualTo(Collections.singletonList(
                new StringValue((new Coach(1L, "55112804", "TrenerИмя111", "TrenerФамилия111").toString()),1L)
        ));
    }

    @Test
    void getTrainingByIdCoachTest() {
        var idCoach = 1L;
        var dataLimit = 3;
        var timeOut = 2;
        var result = webTestClient
                .get().uri(String.format("/v1/MyTrainingCoach/%s", idCoach))
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(StringValue.class)
                .getResponseBody()
                .take(dataLimit)
                .timeout(Duration.ofSeconds(timeOut))
                .collectList()
                .block();
        assertThat(result).hasSize(dataLimit);
    }

    @Test
    void getTrainingByIdStudyTest() {
        var idStudy = 1L;
        var dataLimit = 2;
        var timeOut = 2;
        var result = webTestClient
                .get().uri(String.format("/v1/MyTraining/%s", idStudy))
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(StringValue.class)
                .getResponseBody()
                .take(dataLimit)
                .timeout(Duration.ofSeconds(timeOut))
                .collectList()
                .block();
        assertThat(result).hasSize(dataLimit);
    }

    @Test
    void saveSignOnTrainingTest() {
        var idStudy = 1L;
        var idTraining = 1L;
        var dataLimit = 1;
        var timeOut = 2;
        var result = webTestClient
                .post().uri(String.format("/v1/signtraining/%s/%s", idTraining, idStudy))
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody()
                .take(dataLimit)
                .timeout(Duration.ofSeconds(timeOut))
                .collectList()
                .block();
        assertThat(result).hasSize(dataLimit);
        assertThat(result).isEqualTo(
                Collections.singletonList(1L)
        );
    }
}