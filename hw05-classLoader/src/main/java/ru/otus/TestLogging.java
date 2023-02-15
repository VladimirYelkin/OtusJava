package ru.otus;

import ru.otus.annotations.Log;

import java.io.Serializable;

public class TestLogging implements TestLoggingInterface, Serializable {

    @Log
    public void calculation(int param) {
        System.out.println("calculation origin, param = " +param);
    }

    @Override

    public void calculation(int param1, int param2) {
        System.out.println("calculation origin, param1 = " + param1 + " param2 = " + param2);
    }

    @Override
    @Log
    public void calculation(int param1, int param2, String paramString) {
        System.out.println("calculation origin, param1 = " + param1 + " param2 = " + param2 + " param3 = " + paramString);
    }

    @Override
    @Log
    public void calculation(double param1, double param) {

    }
}
