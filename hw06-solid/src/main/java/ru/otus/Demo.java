package ru.otus;

import java.util.stream.Collectors;

public class Demo {

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.run();
    }

    public void run() {

        Atm atm = new AtmImpl.Builder()
                .initCase(new CaseOfBanknotesImp(BanknotesImpl.FIFTY, 5))
                .initCase(new CaseOfBanknotesImp(BanknotesImpl.ONEHUNDRED, 20))
                .initCase(new CaseOfBanknotesImp(BanknotesImpl.TWOHUNDRED, 10))
                .initCase(new CaseOfBanknotesImp(BanknotesImpl.FIVEHUNDRED, 5))
                .initCase(new CaseOfBanknotesImp(BanknotesImpl.ONETHOUSAND, 8))
                .initCase(new CaseOfBanknotesImp(BanknotesImpl.FIVETHOUSAND, 0))
                .build();

        showBalance(atm);
        showPutInAtmBanknote(atm, BanknotesImpl.ONEHUNDRED);
        showPutInAtmBanknote(atm, BanknotesImpl.TWOHUNDRED);
        showGetFromAtmBanknotes(atm, 7550);//1000 осталась 1
        showGetFromAtmBanknotes(atm, 7450);// осталась одна 50 все остальное по 0
        showPutInAtmBanknote(atm, BanknotesImpl.ONETHOUSAND); // кладем 1000
        showPutInAtmBanknote(atm, BanknotesImpl.FIVEHUNDRED); // и 500
        showGetFromAtmBanknotes(atm, 200); // и пытаемся взять 200
        showGetFromAtmBanknotes(atm, 2000); // или 2000
        showPutInAtmBanknote(atm, BanknotesImpl.TWOTHOUSAND); // корзины для 2000 нет - должно быть сообщение об ошибке
        showPutInAtmBanknote(atm, BanknotesImpl.FIVETHOUSAND); // корзина для 5000 есть

    }

    private void showBalance(Atm atm) {
        System.out.println("-- всего денег в Банкомате стало:" + atm.getSumMoney());
    }

    private void showPutInAtmBanknote(Atm atm, Banknotes banknote) {

        System.out.printf("-- принимает ли банкноту %s? ", banknote);
        try {
            atm.receiveBankNote(banknote);
            System.out.println(" да");
        } catch (AtmException e) {
            System.out.println(e.getMessage());
        } finally {
            showBalance(atm);
        }
    }

    private void showGetFromAtmBanknotes(Atm atm, Integer money) {
        System.out.printf("-- пытается выдает банкноты на сумму %s : ", money);
        try {
            System.out.println(atm.giveMoney(money).stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(",", "(", ")")));
        } catch (AtmException e) {
            System.out.println(e.getMessage());
        } finally {
            showBalance(atm);
        }
    }
}
