import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Класс ATMImplTest")
class ATMImplTest {

    @Test
    @DisplayName("Получаем общую сумму денег в АТМ")
    void getSummMoney() {
        ATM atmTest;

        Banknotes typeBanknotesOfCase1 = BanknotesImpl.ONEHUNDRED;
        int numbersBanknotesOfCase1Init = 50;
        CaseOfBanknotes caseOfBanknotes = new CaseOfBanknotesImp(typeBanknotesOfCase1, numbersBanknotesOfCase1Init);

        atmTest = new ATMImpl.Builder().initCase(caseOfBanknotes).build();
        assertThat(atmTest.getSummMoney().equals(typeBanknotesOfCase1.getNominal() * numbersBanknotesOfCase1Init));
    }

    @Test
    @DisplayName("Добавляем купюру в АТМ")
    void receiveBankNote() {
        ATM atmTest;
        Banknotes typeBanknotesOfCase1 = BanknotesImpl.ONEHUNDRED;
        int numbersBanknotesOfCase1Init = 50;
        CaseOfBanknotes caseOfBanknotes = new CaseOfBanknotesImp(typeBanknotesOfCase1, numbersBanknotesOfCase1Init);

        atmTest = new ATMImpl.Builder().initCase(caseOfBanknotes).build();
        atmTest.receiveBankNote(typeBanknotesOfCase1);

    }

    @Test
    @DisplayName("Добавляем купюру в АТМ корзины для которой нет")
    void receiveIllegalBankNote() {
        ATM atmTest;
        Banknotes banknotesOfCase1 = BanknotesImpl.ONEHUNDRED;
        Banknotes illegalBanknote = BanknotesImpl.ONETHOUSAND;
        int numbersBanknotesOfCase1Init = 50;
        CaseOfBanknotes caseOfBanknotes = new CaseOfBanknotesImp(banknotesOfCase1, numbersBanknotesOfCase1Init);

        atmTest = new ATMImpl.Builder().initCase(caseOfBanknotes).build();
        Assertions.assertThatThrownBy(() -> atmTest.receiveBankNote(illegalBanknote)).isInstanceOf(AtmException.class);
    }
}