package ru.otus;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CaseOfBanknotesImpTest {

    private Banknotes banknote50;
    private Banknotes banknote100;
    private Banknotes banknote200;
    private Banknotes banknote500;
    private Banknotes banknote1000;

    @BeforeEach
    void setUp() {
        banknote50 =Mockito.mock(Banknotes.class);
        banknote100 =Mockito.mock(Banknotes.class);
        banknote200 =Mockito.mock(Banknotes.class);
        banknote500 =Mockito.mock(Banknotes.class);
        banknote1000 =Mockito.mock(Banknotes.class);
        Mockito.when(banknote50.getNominal()).thenReturn(50);
        Mockito.when(banknote100.getNominal()).thenReturn(100);
        Mockito.when(banknote200.getNominal()).thenReturn(200);
        Mockito.when(banknote500.getNominal()).thenReturn(500);
        Mockito.when(banknote1000.getNominal()).thenReturn(1000);
    }


    @Test
    @DisplayName("Проверка получения тип купюры из корзины")
    void getBanknotesTest() {
        int col = 100;
        CaseOfBanknotes testObject = new CaseOfBanknotesImp(banknote100, col);
        assertThat(testObject.getTypeBanknotes()).isEqualTo(banknote100);
    }

    @Test
    @DisplayName("Проверка получения суммы денег в корзине")
    void getSummOfMoneyTest() {
        int col = 100;
        CaseOfBanknotes testObject = new CaseOfBanknotesImp(banknote100, col);
        assertThat(testObject.getSumOfMoney()).isEqualTo(banknote100.getNominal() * col);

        col = 300;
        testObject = new CaseOfBanknotesImp(banknote500, col);
        assertThat(testObject.getSumOfMoney()).isEqualTo(banknote500.getNominal() * col);
    }

    @Test
    @DisplayName("Проверка добавления купюры в корзину")
    void putBankNoteTest() {
        int col = 99;
        CaseOfBanknotes testObject = new CaseOfBanknotesImp(banknote100, col);
        assertThat(testObject.putBankNote(banknote100)).isEqualTo(col + 1);
    }

    @Test
    @DisplayName("Проверка добавления неправильной купюры в корзину должен бросить AtmException")
    void putIllegalBankNoteTest() {
        int col = 1;
        CaseOfBanknotes testObject = new CaseOfBanknotesImp(banknote50, col);
        Assertions.assertThatThrownBy(() -> testObject.putBankNote(banknote1000)).isInstanceOf(AtmException.class);
    }

    @Test
    @DisplayName("Проверка получения суммы денег")
    void giveBankNotesTest() {
        int col = 50;
        int getMoney = 100;
        CaseOfBanknotes testObject = new CaseOfBanknotesImp(banknote50, col);
        assertThat(testObject.giveBankNotes(getMoney).size()).isEqualTo(getMoney / banknote50.getNominal());
        assertThat(testObject.giveBankNotes(getMoney)).isEqualTo(Arrays.asList(banknote50,banknote50));
    }

    @Test
    @DisplayName("Проверка получения суммы денег при нехватке в корзине необходимой суммы")
    void giveBankNotesNoMoneyTest() {
        int col = 9;
        CaseOfBanknotes testObject = new CaseOfBanknotesImp(banknote50, col);
        int getMoney = 500;
        assertThat(testObject.giveBankNotes(getMoney).size()).isEqualTo(col);
    }
}