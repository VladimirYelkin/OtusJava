import java.util.ArrayList;
import java.util.List;

public class ATMImpl implements ATM {

    List<CaseOfBanknotes> caseOfBanknotes;

    public ATMImpl(Builder builder) {
        caseOfBanknotes = new ArrayList<>(builder.casesOfBanknotes);
    }

    @Override
    public Integer getSummMoney() {
        return caseOfBanknotes.stream().mapToInt(CaseOfBanknotes::getSummOfMoney).sum();
    }

    @Override
    public void receiveBankNote(Banknotes banknote) {
        for (CaseOfBanknotes caseOfBankNotes : caseOfBanknotes) {
            if (caseOfBankNotes.getBanknotes().equals(banknote)) {
                caseOfBankNotes.putBankNote(banknote);
                return;
            }
        }
        throw new AtmException("Not Found Case for THIS Banknote");
    }

    public static class Builder {
        List<CaseOfBanknotes> casesOfBanknotes = new ArrayList<>();

        Builder initCase(CaseOfBanknotes caseOfBanknotes) {
            this.casesOfBanknotes.add(caseOfBanknotes);
            return this;
        }

        ATM build() {
            return new ATMImpl(this);
        }
    }

}
