package ru.otus.dto;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ClientDto {

    private  Long id;
    private String name;
    private String streetAddress;
    private Set<String> phones;

    public ClientDto(Long id, String name, String streetAddressofAddresses, Set<String> numbersOfPhones) {
        this.id = id;
        this.name = name;
        this.streetAddress = streetAddressofAddresses;
        this.phones = numbersOfPhones;
    }
}
