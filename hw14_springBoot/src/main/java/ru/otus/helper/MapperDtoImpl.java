package ru.otus.helper;

import org.springframework.stereotype.Component;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.dto.ClientDto;

import java.util.stream.Collectors;

@Component
public class MapperDtoImpl implements MapperDto {

    @Override
    public Client toClient(ClientDto clientDto) {
        return new Client(clientDto.getId(), clientDto.getName()
                , new Address(null, clientDto.getStreetAddress(), clientDto.getId())
                , clientDto.getPhones().stream().map(number -> new Phone(null, number, clientDto.getId())).collect(Collectors.toSet()));
    }

    @Override
    public ClientDto toDto(Client client) {
        return new ClientDto(client.getId(), client.getName(), client.getAddress().streetAddress()
                , client.getPhones().stream().map(Phone::number).collect(Collectors.toSet()));
    }
}
