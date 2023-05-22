package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.GenerateSequenceServiceGrpc;
import ru.otus.protobuf.generated.NumberOfSequence;
import ru.otus.protobuf.generated.Sequence;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class RemoteSequenceServiceImpl extends GenerateSequenceServiceGrpc.GenerateSequenceServiceImplBase {


    public RemoteSequenceServiceImpl() {

    }

    @Override
    public void giveSequence(Sequence request, StreamObserver<NumberOfSequence> responseObserver) {
        long beginSequence = request.getBeginSequence();
        long endSequence = request.getEndSequence();
        List<Long> sequence = LongStream.range(beginSequence, endSequence).boxed().collect(Collectors.toList());
        sequence.forEach(it -> {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(2));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            responseObserver.onNext(LongtoNumberSequence(it));
        });
        responseObserver.onCompleted();
    }


    private NumberOfSequence LongtoNumberSequence(long longSequence) {
        return NumberOfSequence.newBuilder()
                .setCurrentNumber(longSequence)
                .build();
    }


}
