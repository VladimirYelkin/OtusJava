package ru.otus;

import java.util.List;

public interface CaseOfBanknotes {
    Banknotes getTypeBanknotes();
    int getSumOfMoney();
    int putBankNote(Banknotes banknote);
    List<Banknotes> giveBankNotes (int money);
}
