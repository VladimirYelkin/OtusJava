package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ClientDto {

    private Long id;
    private String name;
    private String streetAddress;
    private Set<String> phones;

}
