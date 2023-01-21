package homework;


import java.util.*;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    NavigableMap<Customer, String> customers = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        return createMapEntryWithDeepCopyCustomer(customers.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return createMapEntryWithDeepCopyCustomer(customers.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }

    private Map.Entry<Customer, String> createMapEntryWithDeepCopyCustomer(Map.Entry<Customer, String> customerMapEntry) {
        return Optional.ofNullable(customerMapEntry)
                .map(customerEntry ->
                        new AbstractMap.SimpleEntry<>(
                                // тут напрашивается сделать конструктор у Customer c параметром Customer
                                new Customer(customerEntry.getKey().getId(), customerEntry.getKey().getName(), customerEntry.getKey().getScores()),
                                customerEntry.getValue()
                        )
                )
                .orElse(null);
    }
}
