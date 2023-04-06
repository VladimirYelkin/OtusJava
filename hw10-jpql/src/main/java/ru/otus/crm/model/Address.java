package ru.otus.crm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "addresses")
public class Address implements Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String streetAddress;

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", address='" + streetAddress + '\'' +
                '}';
    }

    @Override
    public Address clone() {
        return new Address(this.id,this.streetAddress);
    }

}
