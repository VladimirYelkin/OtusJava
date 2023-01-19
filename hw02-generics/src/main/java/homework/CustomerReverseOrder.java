package homework;


import java.util.*;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
     private final ArrayDeque<Customer> customers = new ArrayDeque<>();
    public void add(Customer customer) {
        customers.push(customer);
    }

    public Customer take() {
        return customers.pop(); // это "заглушка, чтобы скомилировать"
    }
}
