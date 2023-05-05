package ru.otus.controllers;

import org.springframework.web.bind.annotation.*;
import ru.otus.crm.model.Client;
import ru.otus.dto.ClientDto;
import ru.otus.helper.MapperDto;
import ru.otus.services.ClientService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClientRestController {

    private final ClientService clientService;
    private final MapperDto mapperDto;

    public ClientRestController(ClientService clientService, MapperDto mapperDto) {
        this.clientService = clientService; this.mapperDto = mapperDto;
    }

    @GetMapping("/api/client/{id}")
    public ClientDto getClientById(@PathVariable(name = "id") long id) {
        return mapperDto.toDto(clientService.findById(id));
    }

    @GetMapping("/api/client/")
    public List<ClientDto> getAllClients () {return clientService.findAll().stream().map(mapperDto::toDto).collect(Collectors.toList());}

//    @GetMapping("/api/client")
//    public Client getClientByName(@RequestParam(name = "name") String name) {
//        return clientService.findByName(name);
//    }

    @PostMapping("/api/client/")
    public ClientDto saveClient(@RequestBody ClientDto clientDto) {
        return mapperDto.toDto(clientService.save(mapperDto.fromDto(clientDto)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/client/random")
    public Client findRandomClient() {
        return clientService.findRandom();
    }

}
