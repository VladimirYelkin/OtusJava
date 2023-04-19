package ru.otus.cache;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import ru.otus.crm.model.Client;

@DisplayName(" самодельный кэш  должен ")
class MyCacheTest {
    private MyCache<String, Client> cacheTest;

    @BeforeEach
    void setUp() {
        cacheTest = new MyCache<>();
    }

    @Test
    @DisplayName(" хранить ключ и объект и выдавать по ключу ")
    void putAndGetTest() {
        long id = 10;
        Client client = new Client(id,"200");
        cacheTest.put(String.valueOf(id),client);
        assertThat(client).isEqualTo(cacheTest.get(String.valueOf(id)));
    }

    @Test
    @DisplayName(" выдавать null если значения по ключу нет ")
    void getNonExistTest() {
        long id = 10;
        Client client = new Client(id,"200");
        cacheTest.put(String.valueOf(id),client);
        cacheTest.remove(String.valueOf(id));
        assertThat(cacheTest.get(String.valueOf(id+10))).isNull();
    }

    @Test
    @DisplayName(" удалять объект по ключу ")
    void removeTest() {
        long id = 10;
        Client client = new Client(id,"200");
        cacheTest.put(String.valueOf(id),client);
        cacheTest.remove(String.valueOf(id));
        assertThat(cacheTest.get(String.valueOf(id))).isNull();
    }

    @Test
    @DisplayName(" уметь добалять HwListener и вызывать его, если происходят операции put, get ,remove ")
    void addListenerTest() {
        HwListener<String,Client> listenerTest = Mockito.mock(HwListener.class);
        cacheTest.addListener(listenerTest);
        long id = 10;
        Client client = new Client(id,"200");
        cacheTest.put(String.valueOf(id),client);
        Mockito.verify(listenerTest,Mockito.times(1)).notify(String.valueOf(id),client,"put");
        cacheTest.get(String.valueOf(id));
        Mockito.verify(listenerTest,Mockito.times(1)).notify(String.valueOf(id),client,"get");
        cacheTest.remove(String.valueOf(id));
        Mockito.verify(listenerTest,Mockito.times(1)).notify(String.valueOf(id),client,"remove");
    }

    @Test
    @DisplayName(" уметь удалять сохраненный HwListener ")
    void removeListenerTest() {
        HwListener<String,Client> listenerTest = Mockito.mock(HwListener.class);
        cacheTest.addListener(listenerTest);
        long id = 10;
        Client client = new Client(id,"200");
        cacheTest.put(String.valueOf(id),client);
        Mockito.verify(listenerTest,Mockito.times(1)).notify(String.valueOf(id),client,"put");
        //
        cacheTest.removeListener(listenerTest);
        // when not call notify
        cacheTest.get(String.valueOf(id));
        Mockito.verify(listenerTest,Mockito.times(0)).notify(String.valueOf(id),client,"get");
        cacheTest.remove(String.valueOf(id));
        Mockito.verify(listenerTest,Mockito.times(0)).notify(String.valueOf(id),client,"remove");
    }


    @Test
    @DisplayName(" в случае срабатывания GC терять ссылки на объекты ")
    void clearGCObjects() throws InterruptedException {
        int numbersOfObjectsToSave = 10;
        LongStream.range(0, numbersOfObjectsToSave).forEachOrdered(i -> {
            Client client = new Client(i, "testClient" + i);
            cacheTest.put(String.valueOf(i), client);
        });
        List<Client> testList = IntStream.range(0, numbersOfObjectsToSave).mapToObj(i -> cacheTest.get(String.valueOf(i))).collect(Collectors.toList());
        assertThat(testList.size()).isEqualTo(numbersOfObjectsToSave);
        IntStream.range(0, numbersOfObjectsToSave).forEachOrdered(i -> assertThat(testList.get(i)).isNotNull());
        testList.clear();
        System.gc();
        Thread.sleep(3000);
        System.out.println("gc call");
        IntStream.range(0, numbersOfObjectsToSave).mapToObj(i -> cacheTest.get(String.valueOf(i))).forEachOrdered(testList::add);
        assertThat(testList.size()).isEqualTo(numbersOfObjectsToSave);
        IntStream.range(0, numbersOfObjectsToSave).forEachOrdered(i -> assertThat(testList.get(i)).isNull());
    }
}