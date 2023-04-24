package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.BufferedReader;
import java.io.IOException;



public class ClientsApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final DBServiceClient dbServiceClient;
    private final Gson gson;
    private final TemplateProcessor templateProcessor;

    public ClientsApiServlet(DBServiceClient dbServiceClient, Gson gson, TemplateProcessor templateProcessor) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
        this.templateProcessor=templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long id = extractIdFromRequest(request);
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        if (id != -1L ) {
            var client = dbServiceClient.getClient(id).orElse(null);
            out.print(gson.toJson(client));
        } else  {
            var  clients = dbServiceClient.findAll();
            var clientsJson = clients.stream()
                    .map(gson::toJson)
                    .toList();
            out.print(gson.toJson(clientsJson));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws  IOException {
        BufferedReader reader = request.getReader();
        Client client = gson.fromJson(reader, Client.class);
        Client savedClient = dbServiceClient.saveClient(client);
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(savedClient));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }

}
