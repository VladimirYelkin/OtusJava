package homework;


import java.util.*;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
    // решение : классы реализующие интерфейс Deque:
    // ArrayDeque LinkedList ConcurrentLinkedDeque LinkedBlockingDeque
    private final Deque<Customer> customers = new LinkedList<>();

    public void add(Customer customer) {
        customers.addLast(customer);
    }

    public Customer take() {
        return customers.removeLast();
    }
}
