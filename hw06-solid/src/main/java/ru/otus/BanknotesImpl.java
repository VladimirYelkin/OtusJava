package ru.otus;

public enum BanknotesImpl implements Banknotes {
    FIFTY(50),
    ONEHUNDRED(100),
    TWOHUNDRED(200),
    FIVEHUNDRED(500),
    ONETHOUSAND(1000),
    TWOTHOUSAND(2000),
    FIVETHOUSAND(5000);
    private final int value;

    BanknotesImpl(int value) {
        this.value = value;
    }

    @Override
    public int getNominal() {
        return value;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + value + ")";
    }
}
