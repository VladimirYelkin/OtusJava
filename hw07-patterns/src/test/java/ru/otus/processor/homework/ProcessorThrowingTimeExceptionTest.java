package ru.otus.processor.homework;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.model.Message;

import java.time.DateTimeException;
import java.time.LocalDateTime;

class ProcessorThrowingTimeExceptionTest {

    DataTimeProvider dataTimeProvider;
    Message message ;
    @BeforeEach
    void setUp() {
           dataTimeProvider = Mockito.mock(DataTimeProvider.class);
           message = Mockito.mock(Message.class);
    }

    @Test
    @DisplayName("Тестируем выбрасывания исключения в четную секунду")
    void processTestThrowOnEvenSecond() {
        ProcessorThrowingTimeException testObject = new ProcessorThrowingTimeException(dataTimeProvider);
        Mockito.when(dataTimeProvider.getTime()).thenReturn(LocalDateTime.parse("2023-01-01T10:00:30"));
        Assertions.assertThatThrownBy(() -> testObject.process(message)).isInstanceOf(DateTimeException.class);
    }
    @Test
    @DisplayName("Тестируем отсутствие исключения в нечетную секунду")
    void processTestThrowOffOddSecond() {
        ProcessorThrowingTimeException testObject = new ProcessorThrowingTimeException(dataTimeProvider);
        Mockito.when(dataTimeProvider.getTime()).thenReturn(LocalDateTime.parse("2011-11-11T11:11:11"));
        Assertions.assertThatNoException().isThrownBy(() -> testObject.process(message));
    }
}