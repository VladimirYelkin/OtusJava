package ru.otus.crm.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
//@Entity
//@Table(name = "addresses")
public class Address implements Cloneable{

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "address")
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
