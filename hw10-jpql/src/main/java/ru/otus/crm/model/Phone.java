package ru.otus.crm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;



@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "phones")
public class Phone implements Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String phone;

    @ManyToOne
    private Client client;

    public Phone(String phone) {
        this.phone = phone;
    }

    public Phone(Long id, String phone) {
        this.id = id;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public Phone clone()  {
        return new Phone(this.id,this.phone,this.client);
    }


}



