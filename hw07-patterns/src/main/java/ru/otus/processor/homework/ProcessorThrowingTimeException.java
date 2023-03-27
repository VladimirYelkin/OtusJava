package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.DateTimeException;
import java.time.LocalDateTime;

public class ProcessorThrowingTimeException implements Processor {
    private final DataTimeProvider dataTimeProvider;

    public ProcessorThrowingTimeException(DataTimeProvider dataTimeProvider) {
        this.dataTimeProvider = dataTimeProvider;
    }

    @Override
    public Message process(Message message) {
        LocalDateTime dateTime = dataTimeProvider.getTime();
        if ((dateTime.getSecond() & 1) == 0) {
            throw new DateTimeException("even number: " + dateTime);
        }
        return message;
    }
}
