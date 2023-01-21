package homework;


import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    TreeMap<Customer, String> customers = new TreeMap<>((customer1, customer2) -> {
        if (customer1.getScores() <= customer2.getScores()) {
            if (customer1.getScores() < customer2.getScores()) {
                return -1;
            } else return 0;
        } else {
            return 1;
        }
    }
    );

    public Map.Entry<Customer, String> getSmallest() {
        var result = customers.firstEntry();
        Map.Entry<Customer, String> res1;
        if (result != null) {
            res1 = new AbstractMap.SimpleEntry<Customer, String>(
                    new Customer(result.getKey().getId(), result.getKey().getName(), result.getKey().getScores()), result.getValue());
        } else {
            res1 = null;
        }
        return res1;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        var result = customers.higherEntry(customer);
        Map.Entry<Customer, String> res1;
        if (result != null) {
            res1 = new AbstractMap.SimpleEntry<Customer, String>(
                    new Customer(result.getKey().getId(), result.getKey().getName(), result.getKey().getScores()), result.getValue());
        } else {
            res1 = null;
        }
        return res1;
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }
}
