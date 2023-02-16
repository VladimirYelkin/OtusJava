package ru.otus;

import ru.otus.annotations.Log;

public class TestLogging2 implements LoggingMultiplyInterface {


    public void multiply(int param) {
        System.out.println("besCalculation origin, param = " + param);
    }

    @Override

    public void multiply(int param1, int param2) {
        System.out.println("bestCalculation origin, param1 = " + param1 + " param2 = " + param2);
    }

    @Override
    @Log
    public void multiply(int param1, int param2, String paramString) {
        System.out.println("bestCalculation origin, param1 = " + param1 + " param2 = " + param2 + " param3 = " + paramString);
    }

    @Log
    public void multiply(double param1, double param) {

    }
}
