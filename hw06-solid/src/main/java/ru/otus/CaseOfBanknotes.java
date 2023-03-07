package ru.otus;

import java.util.List;

public interface CaseOfBanknotes {
    Banknotes getTypeBanknotes();
    Integer getSumOfMoney();
    Integer putBankNote(Banknotes banknote);
    List<Banknotes> giveBankNotes (Integer money);
}
