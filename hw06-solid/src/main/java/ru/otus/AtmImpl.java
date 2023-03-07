package ru.otus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

public class AtmImpl implements Atm {

    List<CaseOfBanknotes> caseOfBanknotes;

    public AtmImpl(Builder builder) {
        caseOfBanknotes = builder.casesOfBanknotes.stream()
                .sorted((cs1, cs2) -> cs2.getTypeBanknotes().getNominal() - cs1.getTypeBanknotes().getNominal())
                .collect(Collectors.toList());
    }

    @Override
    public Integer getSumMoney() {
        return caseOfBanknotes.stream().mapToInt(CaseOfBanknotes::getSumOfMoney).sum();
    }

    @Override
    public void receiveBankNote(Banknotes banknote) {
        for (CaseOfBanknotes caseOfBankNotes : caseOfBanknotes) {
            if (caseOfBankNotes.getTypeBanknotes().equals(banknote)) {
                caseOfBankNotes.putBankNote(banknote);
                return;
            }
        }
        throw new AtmException("Not Found Case for THIS Banknote");
    }


    public List<Banknotes> giveMoney(Integer money) {
        if (money > getSumMoney()) {
            throw new AtmException("ATM does not have the necessary amount of money");
        }

        Banknotes minTypeOfBanknote = caseOfBanknotes.stream()
                .filter(caseOfBanknotes1 -> caseOfBanknotes1.getSumOfMoney() > 0)
                .min(comparingInt(cs -> cs.getTypeBanknotes().getNominal()))
                .get().getTypeBanknotes();
        if ((money % minTypeOfBanknote.getNominal()) != 0) {
            throw new AtmException("the amount of money should be divided by " + minTypeOfBanknote.getNominal());
        }

        List<Banknotes> result = new ArrayList<>();
        for (var cs : caseOfBanknotes) {
            var buff = cs.giveBankNotes(money);
            money = money - cs.getTypeBanknotes().getNominal() * buff.size();
            result.addAll(buff);
        }
        if (money != 0) {
            result.forEach(this::receiveBankNote);
            throw new AtmException("No required bank note combination to issue");
        }
        return result;
    }


    public static class Builder {
        List<CaseOfBanknotes> casesOfBanknotes = new ArrayList<>();

        public Builder initCase(CaseOfBanknotes caseOfBanknotes) {
            this.casesOfBanknotes.add(caseOfBanknotes);
            return this;
        }

        public Atm build() {
            return new AtmImpl(this);
        }
    }

}
