package ru.otus;

import java.util.List;

public class AtmImpl implements Atm {

    private final  StorageCases storageCases;

    public AtmImpl(StorageCases storageCases) {
        this.storageCases = storageCases;
    }


    @Override
    public Integer getSumMoney() {
        return storageCases.getSumMoney();
    }

    @Override
    public void receiveBankNote(Banknotes banknote) {
        storageCases.receiveBankNote(banknote);
    }


    public List<Banknotes> giveMoney(Integer money) {
        return storageCases.giveMoney(money);
    }

}
