package ru.otus;

public class StarterDynamicProxyExamples {
    public static void main(String[] args) {

        System.out.println("1. --- proxy with class and interface name installed inside ---");
        LoggingSummaryInterface myTestClass = ProxyOnlyTestLoggingClass.createMyClass();
        myTestClass.summary(6);
        myTestClass.summary(6,7);
        myTestClass.summary(6,7,"Eight");
        myTestClass.summary(6.001, 7.002);


        System.out.println("\n 2. --- proxy with Pass through class and interface name ---");

        System.out.println("2.1 TestLogging class and LoggingSummaryInterface");

        LoggingSummaryInterface myTestClas2 = ProxyClassPassThrough.proxingMyClass(new TestLogging(), LoggingSummaryInterface.class);
        myTestClas2.summary(1777);
        myTestClas2.summary(1,2,"Dynamic Proxy");

        System.out.println("\n 2.2 TestLogging2 class and LoggingMultiplyInterface");

        LoggingMultiplyInterface myTest3 = ProxyClassPassThrough.proxingMyClass(new TestLogging2(), LoggingMultiplyInterface.class);
        myTest3.multiply(1);
        myTest3.multiply(45,12);
        myTest3.multiply(1,2,"Dynamic Proxy");
        myTest3.multiply(1f,3f);

        System.out.println("\n 2.3 TestLogging2 class and LoggingSummaryInterface");
        LoggingSummaryInterface myTest4 = ProxyClassPassThrough.proxingMyClass(new TestLogging2(), LoggingSummaryInterface.class);
        myTest4.summary(6);
        myTest4.summary(6,7);
        myTest4.summary(6,7,"Eight");
        myTest4.summary(6.001, 7.002);
        myTest4.summary(new int[]{0, 1, 2, 3});



        System.out.println("\n3. - One object - two proxy ------");
        TestLogging2 testClassA = new TestLogging2();
        LoggingSummaryInterface myTestClassA1 = ProxyClassPassThrough.proxingMyClass (testClassA, LoggingSummaryInterface.class);
        myTestClassA1.summary(10);
        myTestClassA1.summary(15,12);
        myTestClassA1.summary(1,2,"OneOneOne");

        LoggingMultiplyInterface myTestClassA2 =  ProxyClassPassThrough.proxingMyClass (testClassA, LoggingMultiplyInterface.class);
        myTestClassA2.multiply(1000);
        myTestClassA2.multiply(4500,1200);
        myTestClassA2.multiply(100,200,"Dynamic Proxy ");
        myTestClassA2.multiply(1d,0/0d);

    }
}



