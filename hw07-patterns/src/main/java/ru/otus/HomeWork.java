package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.homework.ProcessorSwapValuesFields11and12;
import ru.otus.processor.homework.ProcessorThrowingTimeException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
             Секунда должна определяьться во время выполнения.
             Тест - важная часть задания
             Обязательно посмотрите пример к паттерну Мементо!
       4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
          Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
          Для него уже есть тест, убедитесь, что тест проходит
     */

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */

        var processors = List.of(new ProcessorThrowingTimeException(() -> LocalDateTime.parse("2023-03-24T12:55:31")), new ProcessorSwapValuesFields11and12(),
                 new ProcessorThrowingTimeException(() -> LocalDateTime.parse("2023-03-24T12:55:30")), new ProcessorThrowingTimeException(LocalDateTime::now));

        var complexProcessor = new ComplexProcessor(processors, ex ->  System.err.println(ex.getMessage()));
        var listenerPrinter = new ListenerPrinterConsole();
        var historyListener = new HistoryListener();
        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(historyListener);
        long id = 111L;
        var data = "hOmEwOrK7-patterns";
        var field13 = new ObjectForMessage();
        var field13Data = new ArrayList<String>();
        field13Data.add(data);
        field13.setData(field13Data);

        var message = new Message.Builder(id)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(field13)
                .build();

        message = complexProcessor.handle(message);
        System.out.println("result:" + message);

        System.out.println("change field13:");
        message.getField13().setData(new LinkedList<>(List.of("Change","filed13")));

        System.out.println("history:" + historyListener.findMessageById(id).map(Object::toString).orElse("нет сообщения в истории"));
        System.out.println("result:" + message);


    }
}

