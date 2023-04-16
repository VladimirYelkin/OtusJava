package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.HwCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

// run test from gradle
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private final HwCache<String, Client> cache;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this(transactionManager, clientDataTemplate, null);
    }

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate, HwCache<String, Client> kvMyCache) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.cache = kvMyCache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                saveClientToCache(clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", clientCloned);
            saveClientToCache(clientCloned);
            return clientCloned;
        });
    }


    @Override
    public Optional<Client> getClient(long id) {
        var clientFromCache = getClientFromCache(String.valueOf(id));
        log.info("client in cache?: id={} value={}", id, clientFromCache);
        if (clientFromCache.isPresent()) {
            return clientFromCache;
        }
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            clientOptional.ifPresent(this::saveClientToCache);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }

    private Optional<Client> getClientFromCache(String id) {
        return Optional.ofNullable(cache).map(cache1 -> cache1.get(id));
    }

    private void saveClientToCache(Client client) {
        Optional.ofNullable(cache).ifPresent(cache1 -> cache1.put(String.valueOf(client.getId()), client));
    }
}
