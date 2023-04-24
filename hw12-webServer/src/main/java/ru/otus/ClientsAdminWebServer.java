package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.server.ClientsWebServerWithBasicSecurity;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

import java.util.Collections;
import java.util.List;

/*
    Запуск PostgreSQL в докере:
    docker run --rm --name pg-docker -e POSTGRES_PASSWORD=pwd -e POSTGRES_USER=usr -e POSTGRES_DB=demoDB -p 5430:5432 postgres:12

    // Стартовая страница
    http://localhost:8080

    basicsecurity: login: editor  password: 12345


*/
public class ClientsAdminWebServer {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "ClientServerRealm";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";


    public static void main(String[] args) throws Exception {
        DBServiceClient dbServiceClient = initDbServiceClient();

        createDummyClientsInDB(dbServiceClient);//for demo

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);

        ru.otus.server.WebServer clientsWebServer = new ClientsWebServerWithBasicSecurity(WEB_SERVER_PORT,
                loginService, dbServiceClient, gson, templateProcessor);

        clientsWebServer.start();
        clientsWebServer.join();
    }

    private static DBServiceClient initDbServiceClient() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        return new DbServiceClientImpl(transactionManager, clientTemplate);
    }

    private static void createDummyClientsInDB(DBServiceClient dbServiceClient) {
        dbServiceClient.saveClient(
                new Client(null, "First", new Address(null, "City A StreetAA"), Collections.singletonList(new Phone(null, "1-111-111")))
        );
        dbServiceClient.saveClient(
                new Client(null, "Second", new Address(null, "City B StreetBB"), Collections.singletonList(new Phone(null, "2-22-222")))
        );
        dbServiceClient.saveClient(
                new Client(null, "Third", new Address(null, "City C StreetCC"), List.of(new Phone(null, "3-33-333"),new Phone(null, "4-44-444")))
        );
    }
}
