package ru.otus;

public class StarterDynamicProxyExamples {
    public static void main(String[] args) {
        TestLoggingInterface myTestClass = ProxyMyClass.createMyClass();
        myTestClass.calculation(6);
        myTestClass.calculation(6,7);
        myTestClass.calculation(6,7,"Eight");
        myTestClass.calculation(6.001, 7.002);

//        System.out.println(ProxyMyClass.DemoInvocationHandler);

    }
}



