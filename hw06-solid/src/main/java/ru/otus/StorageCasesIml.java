package ru.otus;

import java.util.*;


import static java.util.Comparator.comparingInt;

public class StorageCasesIml implements StorageCases {

    private final SortedMap<Banknotes, CaseOfBanknotes> casesSortedBanknotes;
    public StorageCasesIml(SortedMap<Banknotes, CaseOfBanknotes> casesSortedBanknotes) {
        this.casesSortedBanknotes =casesSortedBanknotes;
    }

    @Override
    public Integer getSumMoney() {
        return casesSortedBanknotes.values().stream().mapToInt(CaseOfBanknotes::getSumOfMoney).sum();
    }

    @Override
    public void receiveBankNote(Banknotes banknote) {
        if (casesSortedBanknotes.containsKey(banknote)) {
            casesSortedBanknotes.get(banknote).putBankNote(banknote);
            return;
        }
        throw new AtmException("Not Found Case for THIS Banknote");
    }


    public List<Banknotes> giveMoney(Integer money) {
        if (money > getSumMoney() || getSumMoney() == 0) {
            throw new AtmException("ATM does not have the necessary amount of money");
        }

        var minTypeOfBanknote = casesSortedBanknotes.entrySet().stream()
                .filter(e -> e.getValue().getSumOfMoney() >0)
                .map(Map.Entry::getKey).min(comparingInt(Banknotes::getNominal)).orElse(null);
        if (minTypeOfBanknote == null) {
            throw new AtmException("the amount of money should be divided by ");
        }
        if ((money % minTypeOfBanknote.getNominal()) != 0) {
            throw new AtmException("the amount of money should be divided by " + minTypeOfBanknote.getNominal());
        }

        List<Banknotes> result = new ArrayList<>();
        for (var cs : casesSortedBanknotes.values()) {
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
        private final SortedMap<Banknotes,CaseOfBanknotes> sortedCasesOfBanknotes = new TreeMap<>((bnkt1, bnkt2) -> bnkt2.getNominal() - bnkt1.getNominal());

        public Builder initCase(CaseOfBanknotes caseOfBanknotes) {
            this.sortedCasesOfBanknotes.put(caseOfBanknotes.getTypeBanknotes(), caseOfBanknotes);
            return this;
        }

        public StorageCases build() {
            return new StorageCasesIml(this.sortedCasesOfBanknotes);
        }


    }

}
