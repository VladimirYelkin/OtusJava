package ru.otus;

import ru.otus.annotations.Log;

public class TestLogging implements LoggingSummaryInterface {

    @Override
    public void summary(int[] arr) {
        System.out.println("calculation origin, param1 = arrays");
    }

    @Log
    public void summary(int param) {
        System.out.println("calculation origin, param = " +param);
    }

    @Override

    public void summary(int param1, int param2) {
        System.out.println("calculation origin, param1 = " + param1 + " param2 = " + param2);
    }

    @Override
    @Log
    public void summary(int param1, int param2, String paramString) {
        System.out.println("calculation origin, param1 = " + param1 + " param2 = " + param2 + " param3 = " + paramString);
    }

    @Override
    @Log
    public void summary(double param1, double param) {

    }
}
