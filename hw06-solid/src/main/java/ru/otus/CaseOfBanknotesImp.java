package ru.otus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CaseOfBanknotesImp implements CaseOfBanknotes {
    private final Banknotes banknotes;
    private int numbersBanknotes;

    public CaseOfBanknotesImp(Banknotes banknotes, int numbersBanknotes) {
        this.banknotes = banknotes;
        this.numbersBanknotes = numbersBanknotes;
    }

    @Override
    public List<Banknotes> giveBankNotes(int money) {
        List<Banknotes> banknotesOut = new ArrayList<>();
        if (numbersBanknotes > 0 && money > 0) {
            int needNumbersOfBanknotes = money / banknotes.getNominal();
            int numberOfBanknotestoGive = Math.min(needNumbersOfBanknotes, numbersBanknotes);
            banknotesOut = IntStream.range(0, numberOfBanknotestoGive).mapToObj(i -> banknotes).collect(Collectors.toList());
            numbersBanknotes -= numberOfBanknotestoGive;
        }
        return banknotesOut;
    }

    @Override
    public Banknotes getTypeBanknotes() {
        return banknotes;
    }

    @Override
    public int getSumOfMoney() {
        return numbersBanknotes * banknotes.getNominal();
    }

    @Override
    public int putBankNote(Banknotes banknote) {
        if (banknote != banknotes) {
            throw new AtmException("Illegal BankNote");
        }
        return ++numbersBanknotes;
    }
}
