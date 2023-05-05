package ru.otus.services;

import ru.otus.crm.model.Client;
import ru.otus.repository.ClientRepository;

import java.util.List;
import java.util.Optional;


public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> findById(long id) {
        return clientRepository.findById(id);
    }



    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }
}
