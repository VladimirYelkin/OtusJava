package ru.otus.controllers;

import org.springframework.web.bind.annotation.*;
import ru.otus.dto.ClientDto;
import ru.otus.helper.MapperDto;
import ru.otus.crm.services.ClientService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClientRestController {

    private final ClientService clientService;
    private final MapperDto mapperDto;

    public ClientRestController(ClientService clientService, MapperDto mapperDto) {
        this.clientService = clientService;
        this.mapperDto = mapperDto;
    }

    @GetMapping("/api/client/{id}")
    public ClientDto getClientById(@PathVariable(name = "id") long id) {
        var client = clientService.findById(id);
        return client.map(mapperDto::toDto).orElse(null);
    }

    @GetMapping("/api/client/")
    public List<ClientDto> getAllClients() {
        return clientService.findAll().stream().map(mapperDto::toDto).collect(Collectors.toList());
    }

    @PostMapping("/api/client/")
    public ClientDto saveClient(@RequestBody ClientDto clientDto) {
        return mapperDto.toDto(clientService.save(mapperDto.toClient(clientDto)));
    }



}
