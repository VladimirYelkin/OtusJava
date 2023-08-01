package ru.otus.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateRequest {
    private static final Logger log = LoggerFactory.getLogger(CreateRequest.class);

    public static RequestToDataStore byCommand(String firstWordOfMessage) {
        RequestToDataStore requestImpl = new RequestImpl();

        if ("/start".equals(firstWordOfMessage)) {
            requestImpl.setGetData(new GetInfoUser());
        } else if ("/mytrainig".equals(firstWordOfMessage)) {
            requestImpl.setGetData(new GetMyTrainingList());
        } else if ("/traininglist".equals(firstWordOfMessage)) {
            requestImpl.setGetData(new GetTrainingList());
        } else if (firstWordOfMessage.startsWith("/rec")) {
            requestImpl.setGetData(new SaveOnTraining());
        } else {
            requestImpl.setGetData(new GetEmpty());
        }
        return requestImpl;
    }
}
