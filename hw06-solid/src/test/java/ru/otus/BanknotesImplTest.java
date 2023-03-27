package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BanknotesImplTest {

    @Test
    @DisplayName("Проверка класса купюр")
    void getNominalTest() {
        Banknotes testObject = BanknotesImpl.FIFTY;
        assertEquals(testObject.getNominal(), 50);

        testObject = BanknotesImpl.ONEHUNDRED;
        assertEquals(testObject.getNominal(), 100);

        testObject = BanknotesImpl.TWOHUNDRED;
        assertEquals(testObject.getNominal(), 200);

        testObject = BanknotesImpl.ONETHOUSAND;
        assertEquals(testObject.getNominal(), 1000);

        testObject = BanknotesImpl.TWOTHOUSAND;
        assertEquals(testObject.getNominal(), 2000);

        testObject = BanknotesImpl.FIVETHOUSAND;
        assertEquals(testObject.getNominal(), 5000);
    }
}