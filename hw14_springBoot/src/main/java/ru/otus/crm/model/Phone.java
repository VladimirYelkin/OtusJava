package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.Objects;

@Table(name = "phones")
public class Phone {
    @Id
    private final Long id;
    @NonNull
    private final String number;
    @NonNull
    private final Long clientId;

    public Phone(Long id, @NonNull String number, @NonNull Long clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    @Id
    public Long id() {
        return id;
    }

    @NonNull
    public String number() {
        return number;
    }

    @NonNull
    public Long clientId() {
        return clientId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Phone) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.number, that.number) &&
                Objects.equals(this.clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, clientId);
    }

    @Override
    public String toString() {
        return "Phone[" +
                "id=" + id + ", " +
                "number=" + number + ", " +
                "clientId=" + clientId + ']';
    }

}



