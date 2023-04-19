package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.HwCache;
import ru.otus.cache.MyCache;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DbServiceDemoWithCache {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemoWithCache.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        HwCache<String, Client> cache = new MyCache<>();

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate, cache);
        List<Client> savedClientList = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            var client = new Client(null, "Vasya" + i + (Math.random() * 1000), new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"),
                    new Phone(null, "14-666-333")));
            var savedClient = dbServiceClient.saveClient(client);
            savedClientList.add(savedClient);
        }

        log.info("SAVED CLIENT LIST {} Prepare statement count {}", savedClientList.size(), sessionFactory.getStatistics().getPrepareStatementCount());

        log.info("-----------------------------------------------------");

        sessionFactory.getStatistics().clear();
        List<Client> loadedClientList = new ArrayList<>();
        for (var clientA : savedClientList) {
            var loadedClient = dbServiceClient.getClient(clientA.getId());
            loadedClient.ifPresent(loadedClientList::add);
        }


        log.info("size = loadedClient list {} , Prepare statement count {}", loadedClientList.size(), sessionFactory.getStatistics().getPrepareStatementCount());


        dbServiceClient.saveClient(new Client(null, "dbServiceFirst", new Address(null, "address1")
                , Collections.singletonList(new Phone(null, "0-1-222"))));

        var clientSecond = dbServiceClient.saveClient(new Client(null, "dbServiceSecond", new Address(null, "address2")
                , Collections.singletonList(new Phone(null, "12334"))
        ));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);
///
        var clientSecondID = dbServiceClient.saveClient(new Client(clientSecondSelected.getId(), "dbServiceSecondUpdated"));
        clientSecondID.setPhones(null);
        var clientUpdated = dbServiceClient.getClient(clientSecondID.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondID.getId()));
        log.info("clientUpdated:{}", clientUpdated);
    }
}
