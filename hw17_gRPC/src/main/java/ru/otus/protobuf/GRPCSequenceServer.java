package ru.otus.protobuf;


import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.service.RemoteSequenceServiceImpl;

import java.io.IOException;

public class GRPCSequenceServer {

    public static final int SERVER_PORT = 8190;
    private static final Logger logger = LoggerFactory.getLogger(GRPCSequenceServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {

        var remoteSequenceService = new RemoteSequenceServiceImpl();

        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteSequenceService).build();
        server.start();
        logger.info("server waiting for client connections...");
        server.awaitTermination();
    }
}
