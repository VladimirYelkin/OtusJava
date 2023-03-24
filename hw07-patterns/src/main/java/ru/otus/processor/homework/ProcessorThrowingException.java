package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.DateTimeException;

public class ProcessorThrowingException implements Processor {
    DataTimeProvider dataTimeProvider;

    public ProcessorThrowingException(DataTimeProvider dataTimeProvider) {
        this.dataTimeProvider = dataTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if ((dataTimeProvider.getTime().getSecond() & 1) == 0) {
            throw new DateTimeException("even number");
        }
        return message;
    }
}
