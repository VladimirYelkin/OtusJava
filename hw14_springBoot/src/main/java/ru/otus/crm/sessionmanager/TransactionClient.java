package ru.otus.crm.sessionmanager;

public interface TransactionClient {

    <T> T doInTransaction(TransactionAction<T> action);
}
