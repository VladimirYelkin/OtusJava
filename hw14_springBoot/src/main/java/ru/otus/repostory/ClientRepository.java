package ru.otus.repostory;

import ru.otus.crm.model.Client;

import java.util.List;

public interface ClientRepository {

    List<Client> findAll();

    Client save(Client client);

    Client findById(long id);

    Client findByName(String name);
}
