package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.Objects;

@Table(name = "addresses")
public class Address {
    @Id
    private final Long id;
    @NonNull
    @Column("address")
    private final String streetAddress;
    @NonNull
    private final Long clientId;

    public Address(Long id, @NonNull String streetAddress, @NonNull Long clientId) {
        this.id = id;
        this.streetAddress = streetAddress;
        this.clientId = clientId;
    }

    @Id
    public Long id() {
        return id;
    }

    @NonNull
    @Column("address")
    public String streetAddress() {
        return streetAddress;
    }

    @NonNull
    public Long clientId() {
        return clientId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Address) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.streetAddress, that.streetAddress) &&
                Objects.equals(this.clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, streetAddress, clientId);
    }

    @Override
    public String toString() {
        return "Address[" +
                "id=" + id + ", " +
                "streetAddress=" + streetAddress + ", " +
                "clientId=" + clientId + ']';
    }

}