package ru.otus.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ClientDto {

    private Long id;
    private String name;
    private String streetAddress;
    private List<String> phones;

    public ClientDto(Long id, String name, String streetAddressofAddresses, List<String> numbersOfPhones) {
        this.id = id;
        this.name = name;
        this.streetAddress = streetAddressofAddresses;
        this.phones = numbersOfPhones;
    }
}
