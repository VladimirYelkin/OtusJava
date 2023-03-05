public class CaseOfBanknotesImp implements CaseOfBanknotes{
    private final Banknotes banknotes;
    private int numbersBanknotes;

    public CaseOfBanknotesImp(Banknotes banknotes, int numbersBanknotes) {
        this.banknotes = banknotes;
        this.numbersBanknotes = numbersBanknotes;
    }

    @Override
    public Banknotes getBanknotes() {
        return banknotes;
    }

    @Override
    public Integer getSummOfMoney() {
        return numbersBanknotes* banknotes.getNominal();
    }

    @Override
    public Integer putBankNote(Banknotes banknote) {
        if (banknote!= banknotes) {
            throw new AtmException("Illegal BankNote");
        }
        return ++numbersBanknotes;
    }
}
