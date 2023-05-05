package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.crm.model.Client;
import ru.otus.services.ClientService;

@Controller
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping({"/","/index","index.hml"})
    public String startView(Model model) {
        return "index";
    }

    @GetMapping("/admin")
    public String clientCreateView(Model model) {
//        model.addAttribute("client", new Client());
        return "tools";
    }

//    @PostMapping("/client/save")
//    public RedirectView clientSave(@ModelAttribute Client client) {
//        clientService.save(client);
//        return new RedirectView("/", true);
//    }

}
