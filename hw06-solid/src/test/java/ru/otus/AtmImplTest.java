package ru.otus;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@DisplayName("Класс ATMImplTest")
class AtmImplTest {
    private Banknotes banknote50;
    private Banknotes banknote100;
    private Banknotes banknote200;
    private Banknotes banknote500;
    private Banknotes banknote1000;

    private CaseOfBanknotes case50;
    private CaseOfBanknotes case100;
    private CaseOfBanknotes case200;
    private CaseOfBanknotes case500;

    @BeforeEach
    void setUp() {
        banknote50 = Mockito.mock(Banknotes.class);
        banknote100 = Mockito.mock(Banknotes.class);

        banknote200 = Mockito.mock(Banknotes.class);
        banknote500 = Mockito.mock(Banknotes.class);
        banknote1000 = Mockito.mock(Banknotes.class);
        Mockito.when(banknote50.getNominal()).thenReturn(50);
        Mockito.when(banknote50.toString()).thenReturn("50");

        Mockito.when(banknote100.getNominal()).thenReturn(100);
        Mockito.when(banknote100.toString()).thenReturn("100");

        Mockito.when(banknote200.getNominal()).thenReturn(200);
        Mockito.when(banknote200.toString()).thenReturn("200");

        Mockito.when(banknote500.getNominal()).thenReturn(500);
        Mockito.when(banknote500.toString()).thenReturn("500");

        Mockito.when(banknote1000.getNominal()).thenReturn(1000);
        Mockito.when(banknote1000.toString()).thenReturn("1000");

        case50 = Mockito.mock(CaseOfBanknotes.class);
        Mockito.when(case50.getTypeBanknotes()).thenReturn(banknote50);
        case100 = Mockito.mock(CaseOfBanknotes.class);
        Mockito.when(case100.getTypeBanknotes()).thenReturn(banknote100);

        case200 = Mockito.mock(CaseOfBanknotes.class);
        Mockito.when(case200.getTypeBanknotes()).thenReturn(banknote200);
        case500 = Mockito.mock(CaseOfBanknotes.class);
        Mockito.when(case500.getTypeBanknotes()).thenReturn(banknote500);
        CaseOfBanknotes case1000 = Mockito.mock(CaseOfBanknotes.class);
        Mockito.when(case1000.getTypeBanknotes()).thenReturn(banknote1000);
    }

    @Test
    @DisplayName("Создание объекта АТМ")
    void createAtmTest() {

        Atm atmTest = new AtmImpl.Builder().initCase(case100).initCase(case500).initCase(case50).build();
        assertThat(atmTest).isInstanceOf(Atm.class);
    }

    @Test
    @DisplayName("Получаем общую сумму денег в АТМ")
    void getSumMoneyTest() {
        Atm atmTest;

        int numbersBanknotesOfCase1Init = 50;
        int nominalBanknotesOfCase1 = case100.getTypeBanknotes().getNominal();
        Mockito.when(case100.getSumOfMoney()).thenReturn(numbersBanknotesOfCase1Init * nominalBanknotesOfCase1);
        CaseOfBanknotes caseOfBanknotes1 = case100;

        int numbersBanknotesOfCase2Init = 100;
        int nominalBanknotesOfCase2 = case500.getTypeBanknotes().getNominal();
        Mockito.when(case500.getSumOfMoney()).thenReturn(nominalBanknotesOfCase2 * numbersBanknotesOfCase2Init);
        CaseOfBanknotes caseOfBanknotes2 = case500;

        int numbersBanknotesOfCase3Init = 700;
        int nominalBanknotesOfCase3 = case50.getTypeBanknotes().getNominal();
        Mockito.when(case50.getSumOfMoney()).thenReturn(numbersBanknotesOfCase3Init * nominalBanknotesOfCase3);
        CaseOfBanknotes caseOfBanknotes3 = case50;

        atmTest = new AtmImpl.Builder().initCase(caseOfBanknotes1).initCase(caseOfBanknotes2).initCase(caseOfBanknotes3).build();
        assertThat(atmTest.getSumMoney())
                .isEqualTo(nominalBanknotesOfCase1 * numbersBanknotesOfCase1Init
                        + nominalBanknotesOfCase2 * numbersBanknotesOfCase2Init
                        + nominalBanknotesOfCase3 * numbersBanknotesOfCase3Init);
    }

    @Test
    @DisplayName("Добавляем купюру в АТМ")
    void receiveBankNote() {
        Atm atmTest;
        InOrder inOrderCaseOfAdd = Mockito.inOrder(case100);
        InOrder inOrderCaseWithOutAdditional = Mockito.inOrder(case50);
        atmTest = new AtmImpl.Builder().initCase(case100).initCase(case50).build();
        atmTest.receiveBankNote(banknote100);
        inOrderCaseOfAdd.verify(case100, times(1)).putBankNote(banknote100);
        inOrderCaseWithOutAdditional.verify(case50, times(0)).putBankNote(banknote100);
    }

    @Test
    @DisplayName("Добавляем купюру в АТМ корзины для которой нет - ждем AtmException")
    void receiveIllegalBankNote() {
        Atm atmTest;
        atmTest = new AtmImpl.Builder().initCase(case100).build();
        Assertions.assertThatThrownBy(() -> atmTest.receiveBankNote(banknote1000)).isInstanceOf(AtmException.class);
    }

    @Test
    @DisplayName("Получаем банкноты с АТМ")
    void giveMoneyTest() {
        Atm atmTest;

        int numbersBanknotesOfCase1Init = 10;
        int nominalBanknotesOfCase1 = case100.getTypeBanknotes().getNominal();
        Mockito.when(case100.getSumOfMoney()).thenReturn(numbersBanknotesOfCase1Init * nominalBanknotesOfCase1);
        Mockito.when(case100.giveBankNotes(350)).thenReturn(Arrays.asList(banknote100, banknote100, banknote100));

        int numbersBanknotesOfCase2Init = 10;
        int nominalBanknotesOfCase2 = case500.getTypeBanknotes().getNominal();
        Mockito.when(case500.getSumOfMoney()).thenReturn(numbersBanknotesOfCase2Init * nominalBanknotesOfCase2);
        Mockito.when(case500.giveBankNotes(850)).thenReturn(Collections.singletonList(banknote500));

        int numbersBanknotesOfCase3Init = 70;
        int nominalBanknotesOfCase3 = case50.getTypeBanknotes().getNominal();
        Mockito.when(case50.getSumOfMoney()).thenReturn(numbersBanknotesOfCase3Init * nominalBanknotesOfCase3);
        Mockito.when(case50.giveBankNotes(50)).thenReturn(Collections.singletonList(banknote50));

        atmTest = new AtmImpl.Builder().initCase(case100).initCase(case500).initCase(case50).build();
        assertThat(atmTest.giveMoney(850)).isEqualTo(Arrays.asList(banknote500, banknote100, banknote100, banknote100, banknote50));
    }

    @Test
    @DisplayName("Получаем банкноты с АТМ нужных купюр нет - ждем AtmException")
    void giveMoneyTestWithException() {
        Atm atmTest;

        int numbersBanknotesOfCase1Init = 10;
        int nominalBanknotesOfCase1 = case100.getTypeBanknotes().getNominal();
        Mockito.when(case100.getSumOfMoney()).thenReturn(numbersBanknotesOfCase1Init * nominalBanknotesOfCase1);
        Mockito.when(case100.giveBankNotes(350)).thenReturn(Arrays.asList(banknote100, banknote100, banknote100));

        int numbersBanknotesOfCase2Init = 10;
        int nominalBanknotesOfCase2 = case500.getTypeBanknotes().getNominal();
        Mockito.when(case500.getSumOfMoney()).thenReturn(numbersBanknotesOfCase2Init * nominalBanknotesOfCase2);
        Mockito.when(case500.giveBankNotes(850)).thenReturn(Collections.singletonList(banknote500));

        atmTest = new AtmImpl.Builder().initCase(case100).initCase(case500).build();
        Assertions.assertThatThrownBy(() -> atmTest.giveMoney(850)).isInstanceOf(AtmException.class);
    }
}