package ru.otus.crm.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.base.AbstractHibernateTest;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static org.assertj.core.api.Assertions.assertThat;
// test passed with setting in gradle or change run VM properties in IDEA to -ea -Xms256m -Xmx256m

@DisplayName("Демо работы с hibernate (с абстракциями) должно ")
class DbServiceClientTest extends AbstractHibernateTest {
    private static final int EXPECTED_QUERIES_COUNT_WITH_CACHE = 0;
    private static final int EXPECTED_NUMBER_OF_CLIENT = 200;
    private static final int EXPECTED_QUERIES_COUNT_WITHOUT_CACHE = 2 * EXPECTED_NUMBER_OF_CLIENT;


    @Test
    @DisplayName(" корректно сохранять, изменять и загружать клиента")
    void shouldCorrectSaveClient() {
        //given
//        var client = new Client("Ivan");

        // Это надо раскомментировать, у выполненного ДЗ, все тесты должны проходить
        // Кроме удаления комментирования, тестовый класс менять нельзя
        var client = new Client(null, "Vasya", new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"),
                new Phone(null, "14-666-333")));

        //when
        var savedClient = dbServiceClient.saveClient(client);
        System.out.println(savedClient);

        //then
        var loadedSavedClient = dbServiceClient.getClient(savedClient.getId());
        assertThat(loadedSavedClient).isPresent();
        assertThat(loadedSavedClient.get()).usingRecursiveComparison().isEqualTo(savedClient);

        //when
        var savedClientUpdated = loadedSavedClient.get().clone();
        savedClientUpdated.setName("updatedName");
        dbServiceClient.saveClient(savedClientUpdated);

        //then
        var loadedClient = dbServiceClient.getClient(savedClientUpdated.getId());
        assertThat(loadedClient).isPresent();
        assertThat(loadedClient.get()).usingRecursiveComparison().isEqualTo(savedClientUpdated);
        System.out.println(loadedClient);

        //when
        var clientList = dbServiceClient.findAll();

        //then
        assertThat(clientList.size()).isEqualTo(1);
        assertThat(clientList.get(0)).usingRecursiveComparison().isEqualTo(loadedClient.get());
    }

    @Test
    @DisplayName("корректно использовать кэш при сохранение и загрузке")
    void checkCacheWorked() throws InterruptedException {
        var countsOfClientInDbBeforeSavedList = dbServiceClient.findAll().size();

        sessionFactory.getStatistics().clear();
        List<Client> savedClientList = new ArrayList<>();
        for (int i = 0; i < EXPECTED_NUMBER_OF_CLIENT; i++) {
            var client = new Client(null, "Vasya" + i + (Math.random() * 1000), new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"),
                    new Phone(null, "14-666-333")));
            var savedClient = dbServiceClient.saveClient(client);
            savedClientList.add(savedClient);
        }
        System.out.println("getting clients...");
        sessionFactory.getStatistics().clear();

        for (var clientA : savedClientList) {
            var loadedClient = dbServiceClient.getClient(clientA.getId());
            assertThat(loadedClient).isPresent();
            assertThat(loadedClient.get()).usingRecursiveComparison().isEqualTo(clientA);
        }
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT_WITH_CACHE);


        sessionFactory.getStatistics().clear();
        var clonedClients = savedClientList.stream().map(Client::clone).toList();
        savedClientList = null;
        System.out.println("call GC");
        System.gc();
        Thread.sleep(2000);

        for (var clientCopy : clonedClients) {
            var loadedClient = dbServiceClient.getClient(clientCopy.getId());
            assertThat(loadedClient).isPresent();
            assertThat(loadedClient.get()).usingRecursiveComparison().isEqualTo(clientCopy);
        }
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT_WITHOUT_CACHE);

        savedClientList = dbServiceClient.findAll();
        assertThat(savedClientList.size()).isEqualTo(EXPECTED_NUMBER_OF_CLIENT + countsOfClientInDbBeforeSavedList);
    }

    @Test
    @DisplayName("  при использовании кэша время загрузки должно быть меньше, если до этого данные сохранялись или загружались и еще не запускался GC  ")
    void compareTimeOfLoadedClientWithCache() throws InterruptedException {
        Set<Long> setIds = new HashSet<>();
        for (int i = 0; i < EXPECTED_NUMBER_OF_CLIENT; i++) {
            var client = new Client(null, "TimeMeasuring" + i + (Math.random() * 1000), new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"),
                    new Phone(null, "14-666-333")));
            var savedClient = dbServiceClient.saveClient(client);
            setIds.add(savedClient.getId());
        }
        //now cache after save
        long timeOfReadingAfterSave = measuringTime(setIds);
        System.gc();
        Thread.sleep(3000);
        //now cache is empty
        long timeOfReadingWithoutCache = measuringTime(setIds);
        //now cache after reading
        long timeOfReadingWithCache = measuringTime(setIds);

        System.out.println("timeOfReadingAfterSave : " + timeOfReadingAfterSave);
        System.out.println("timeOfReadingWithoutCache : " + timeOfReadingWithoutCache);
        System.out.println("timeOfReadingWithCache : " + timeOfReadingWithCache);

        assertThat(timeOfReadingAfterSave).isLessThan(timeOfReadingWithoutCache);
        assertThat(timeOfReadingWithoutCache).isGreaterThan(timeOfReadingWithCache);
    }

    private long measuringTime(Set<Long> ids) {
        long startTime = System.currentTimeMillis();
        for (var id : ids) {
            dbServiceClient.getClient(id);
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }


}