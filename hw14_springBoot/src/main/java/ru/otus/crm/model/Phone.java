package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;



@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
//@Entity
//@Table(name = "phones")
public class Phone implements Cloneable{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column()
    private String number;


//    @ManyToOne
//    @JoinColumn(name = "client_id")
    private Client client;

    public Phone(String phone) {
        this.number = phone;
    }

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", phone='" + number + '\'' +
                '}';
    }

    @Override
    public Phone clone()  {
        return new Phone(this.id,this.number,this.client);
    }


}



