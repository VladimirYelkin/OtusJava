package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);
    private final BlockingQueue<SensorData> bufferedData;
    private final int bufferSize;
    private final SensorDataBufferedWriter writer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.bufferedData = new PriorityBlockingQueue<>(bufferSize + bufferSize >> 1, Comparator.comparing(SensorData::getMeasurementTime));
    }

    @Override
    public void process(SensorData data) {
        if (bufferedData.size() >= bufferSize) {
            flush();
        }
        if (data != null) {
            try {
                if (!(bufferedData.offer(data)))  {
                    log.error("Ошибка в процессе записи в буфер {}",data);
                }
            } catch (IllegalMonitorStateException e) {
                log.error("Ошибка в процессе записи в буфер", e);
            }
        }
    }

    public void flush() {
        try {
            List<SensorData> buff = new ArrayList<>(bufferSize);
            if (bufferedData.drainTo(buff, bufferSize) != 0) {
                writer.writeBufferedData(buff);
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}