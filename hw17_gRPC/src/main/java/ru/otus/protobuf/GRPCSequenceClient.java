package ru.otus.protobuf;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.GenerateSequenceServiceGrpc;
import ru.otus.protobuf.generated.NumberOfSequence;
import ru.otus.protobuf.generated.Sequence;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class GRPCSequenceClient {
    private static final Logger logger = LoggerFactory.getLogger(GRPCSequenceClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final AtomicLong answer = new AtomicLong();

    public static void main(String[] args) throws InterruptedException {

        var channel = createChannel(SERVER_HOST, SERVER_PORT);
        logger.info("numbers Client is starting...");
        var latch = new CountDownLatch(1);
        var newStub = GenerateSequenceServiceGrpc.newStub(channel);
        newStub.giveSequence(rangeOfSequence(0L, 31L), new StreamObserver<NumberOfSequence>() {

            @Override
            public void onNext(NumberOfSequence value) {
                answer.set(value.getCurrentNumber());
                logger.info("new value from server: {}", answer.get());
            }

            @Override
            public void onError(Throwable t) {
                logger.error("Error:",t);
            }

            @Override
            public void onCompleted() {
                logger.debug("finish");
                latch.countDown();
            }
        });




        latch.await();
        channel.shutdown();
    }


    private static void cycleIncrementByAnswer (long begin,long end) throws InterruptedException {
        long currentValue = 0;
        long answerValue = 0;
        for (int i = 0; i < 51; i++) {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            answerValue = (answerValue == answer.longValue()) ? 0L : answer.longValue();
            logger.info("answerValue {} ", answerValue);
            currentValue = currentValue + 1 + answerValue;

            logger.info("currentValue: {}", currentValue);
        }
    }

    private static ManagedChannel createChannel(String serverHost, int port) {
        return ManagedChannelBuilder.forAddress(serverHost, port)
                .usePlaintext()
                .build();
    }

    private static Sequence rangeOfSequence(long begin, long end) {
        return Sequence.newBuilder()
                .setBeginSequence(begin)
                .setEndSequence(end)
                .build();
    }
}
