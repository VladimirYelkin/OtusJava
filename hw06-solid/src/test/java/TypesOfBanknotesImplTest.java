import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypesOfBanknotesImplTest {

    @Test
    @DisplayName("Проверка класса типа купюр")
    void getNominalTest() {
        Banknotes testObject = BanknotesImpl.FIFTY;
        assertEquals(testObject.getNominal(), 50);

        testObject = BanknotesImpl.ONEHUNDRED;
        assertEquals(testObject.getNominal(), 100);

        testObject = BanknotesImpl.TWOHUNDRED;
        assertEquals(testObject.getNominal(), 200);

        testObject = BanknotesImpl.ONETHOUSAND;
        assertEquals(testObject.getNominal(), 1000);
    }
}