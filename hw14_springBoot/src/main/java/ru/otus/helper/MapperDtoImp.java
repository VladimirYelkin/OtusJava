package ru.otus.helper;

import org.springframework.stereotype.Component;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.dto.ClientDto;

import java.util.stream.Collectors;

@Component
public class MapperDtoImp implements MapperDto {

    @Override
    public Client fromDto(ClientDto clientDto) {
        return new Client(clientDto.getId(),clientDto.getName()
                ,new Address(null,clientDto.getStreetAddress())
                , clientDto.getPhones().stream().map(number -> new Phone(null,number)).collect(Collectors.toList()));
    }

    @Override
    public ClientDto toDto(Client client) {
        return new ClientDto(client.getId(), client.getName(), client.getAddress().getStreetAddress()
                , client.getPhones().stream().map(phone -> phone.getNumber()).collect(Collectors.toList()));
    }
}
