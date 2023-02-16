package ru.otus;

import ru.otus.annotations.Log;

public class TestLogging2 implements LoggingMultiplyInterface, LoggingSummaryInterface {

    @Override
    public void multiply(int param) {
        System.out.println(getID() + " class2 multiply int //not logged");
    }

    @Override
    @Log
    public void multiply(int param1, int param2) {
        System.out.println(getID() + " class2 multiply int,int");
    }

    @Override
    @Log
    public void multiply(int param1, int param2, String paramString) {
        System.out.println(getID() + " class2 multiply int,int,String");
    }

    @Log
    public void multiply(double param1, double param) {
        System.out.println(getID() + " class2 multiply double,double");
    }

    @Override
    @Log
    public void summary(int param) {
        System.out.println(getID() + " class2 summary class2 multiply");

    }

    @Override

    public void summary(int param1, int param2) {
        System.out.println(getID() + " class2 summary int,int  //not logged");
    }

    @Override
    @Log
    public void summary(int param1, int param2, String paramString) {
        System.out.println(getID() + " class2 summary int,int,String");
    }

    @Override
    @Log
    public void summary(double param1, double param2) {
        System.out.println(getID() + " class2 summary double,double");
    }
    @Override
    @Log
    public void summary(int[] arr) {
        System.out.println(getID() + " class2 summary int[]");
    }

    private String getID() {
        return Integer.toHexString(hashCode());
    }
}
