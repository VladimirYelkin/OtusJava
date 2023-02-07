package ru.otus.reflection;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class TestClassHomeWork03Simple {
    @Before
    public void methodBefore() {
        System.out.println("methodBefore1:" + Integer.toHexString(hashCode()));
    }

    @Test
    public void methodTest() {
        System.out.println("methodTest1:" + Integer.toHexString(hashCode()));
    }

    @After
    public void methodAfter() {
        System.out.println("methodAfter1:" + Integer.toHexString(hashCode()));
    }
}
