package ru.otus;
import java.util.List;

public interface Atm {
    Integer getSumMoney();
    void receiveBankNote(Banknotes banknote);
    List<Banknotes> giveMoney(Integer money);
}
