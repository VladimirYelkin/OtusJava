package ru.otus.helper;

import ru.otus.crm.model.Client;
import ru.otus.dto.ClientDto;

public interface MapperDto {
    ClientDto toDto(Client client);
    Client toClient(ClientDto clientDto);
}
