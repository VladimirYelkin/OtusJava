package ru.otus.crm.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

@Getter
@ToString
@EqualsAndHashCode
@Table(name = "addresses")
public class Address {
    @Id
    private final Long id;

    @NonNull
    @Column("address")
    private final String streetAddress;
    @NonNull
    private final Long clientId;

    @PersistenceCreator
    public Address(Long id, @NonNull String streetAddress, @NonNull Long clientId) {
        this.id = id;
        this.streetAddress = streetAddress;
        this.clientId = clientId;
    }
}