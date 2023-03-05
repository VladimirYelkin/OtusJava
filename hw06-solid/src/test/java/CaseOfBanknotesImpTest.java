import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CaseOfBanknotesImpTest {

    @Test
    @DisplayName("Проверка получения тип купюры из корзины")
    void getBanknotesTest() {
        int col = 100;
        BanknotesImpl banknote = BanknotesImpl.ONEHUNDRED;
        CaseOfBanknotes testObject = new CaseOfBanknotesImp(banknote,col);
        assertThat(testObject.getBanknotes().equals(banknote));
    }

    @Test
    @DisplayName("Проверка получения суммы денег в корзине")
    void getSummOfMoneyTest() {
        int col = 100;
        BanknotesImpl banknote = BanknotesImpl.ONEHUNDRED;
        CaseOfBanknotes testObject = new CaseOfBanknotesImp(banknote,col);
        assertThat(testObject.getSummOfMoney().equals(banknote.getNominal()*col));

        col = 300;
        banknote = BanknotesImpl.FIVEHUNDRED;
        testObject = new CaseOfBanknotesImp(banknote,col);
        assertThat(testObject.getSummOfMoney().equals(banknote.getNominal()*col));
    }

    @Test
    @DisplayName("Проверка добавления купюры в корзину")
    void putBankNoteTest() {
        int col = 99;
        BanknotesImpl banknote = BanknotesImpl.ONEHUNDRED;
        CaseOfBanknotes testObject = new CaseOfBanknotesImp(banknote,col);
        assertThat(testObject.putBankNote(banknote).equals(col+1));

        Banknotes illegalBanknote = BanknotesImpl.FIFTY;
        Assertions.assertThatThrownBy(() -> testObject.putBankNote(illegalBanknote)).isInstanceOf(AtmException.class);
    }

    @Test
    @DisplayName("Проверка добавления неправильной купюры в корзину должен бросить AtmException")
    void putIllegalBankNoteTest() {
        int col = 1;
        BanknotesImpl banknote = BanknotesImpl.FIFTY;
        CaseOfBanknotes testObject = new CaseOfBanknotesImp(banknote,col);

        Banknotes illegalBanknote = BanknotesImpl.ONETHOUSAND;
        Assertions.assertThatThrownBy(() -> testObject.putBankNote(illegalBanknote)).isInstanceOf(AtmException.class);
    }
}