package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.crm.services.ClientService;

@Controller
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping({"/","/index","index.hml"})
    public String startView() {
        return "index";
    }

    @GetMapping("/admin")
    public String clientCreateView() {
        return "tools";
    }


}
