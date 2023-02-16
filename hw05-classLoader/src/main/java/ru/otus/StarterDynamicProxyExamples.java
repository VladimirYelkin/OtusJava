package ru.otus;

public class StarterDynamicProxyExamples {
    public static void main(String[] args) {
        LoggingSummaryInterface myTestClass = ProxyOnlyTestLoggingClass.createMyClass();
        myTestClass.summary(6);
        myTestClass.summary(6,7);
        myTestClass.summary(6,7,"Eight");
        myTestClass.summary(6.001, 7.002);

        LoggingSummaryInterface myTestClas2 = ProxyClassPassThrough.proxingMyClass(new TestLogging(), LoggingSummaryInterface.class);
        myTestClas2.summary(1777);
        myTestClas2.summary(1,2,"Dynamic Proxy");

        LoggingMultiplyInterface myTest3 = ProxyClassPassThrough.proxingMyClass(new TestLogging2(), LoggingMultiplyInterface.class);
        myTest3.multiply(1);
        myTest3.multiply(45,12);
        myTest3.multiply(1,2,"Dynamic Proxy");


        LoggingSummaryInterface myTestA1 = ProxyClassPassThrough.proxingMyClass(new TestLogging(), LoggingSummaryInterface.class);
        myTestA1.summary(1,2);
    }
}



