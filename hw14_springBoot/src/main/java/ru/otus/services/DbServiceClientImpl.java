package ru.otus.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.crm.model.Client;
import ru.otus.repository.ClientRepository;
import ru.otus.sessionmanager.TransactionClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DbServiceClientImpl implements ClientService {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final TransactionClient transactionClient;
    private final ClientRepository clientRepository;

    public DbServiceClientImpl(TransactionClient transactionClient, ClientRepository clientRepository) {
        this.transactionClient = transactionClient;
        this.clientRepository = clientRepository;
    }

    @Override
    public Client save(Client client) {
        return transactionClient.doInTransaction(() -> {
            var savedClient = clientRepository.save(client);
            log.info("saved client: {}", savedClient);
            return savedClient;
        });
    }


    @Override
    public List<Client> findAll() {
        var clientList = new ArrayList<Client>();
        clientRepository.findAll().forEach(clientList::add);
        log.info("clientList:{}", clientList);
        return clientList;
    }

    @Override
    public Optional<Client> findById(long id) {
        var clientOptional = clientRepository.findById(id);
        log.info("client: {}", clientOptional);
        return clientOptional;
    }
}
