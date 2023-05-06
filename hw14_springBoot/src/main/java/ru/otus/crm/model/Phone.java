package ru.otus.crm.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;


@Getter
@ToString
@EqualsAndHashCode
@Table(name = "phones")
public class Phone {
    @Id
    private final Long id;
    @NonNull
    private final String number;
    @NonNull
    private final Long clientId;

    @PersistenceCreator
    public Phone(Long id, @NonNull String number, @NonNull Long clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

}



