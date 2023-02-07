package ru.otus.reflection;

import ru.otus.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TestClassHomeWork03 {
    private Map<String, Integer> valueMap = new HashMap<>();

    Function <Integer,Integer> rnd = x -> (int) (Math.random() * x);
    @Before
    public void methodBefore2() {
        valueMap.put("random", rnd.apply(100));
        valueMap.put("random", 50+rnd.apply(30));/* comment for exceptions in test*/
        System.out.println("methodBefore2:" + Integer.toHexString(hashCode()) + "random value" + valueMap.get("random"));
        if (valueMap.get("random") > 80) {
            throw new RuntimeException("simulate resource unavailability (random =" + valueMap.get("random") + ")");
        }
    }

    @Before
    public void methodBefore1() {
        System.out.println("methodBefore1:" + Integer.toHexString(hashCode()));
        valueMap.put("first", 1);
        valueMap.put("second", 2);
    }

    @Test
    public void methodTest1() {
        System.out.println("methodTest1:" + Integer.toHexString(hashCode()) + " value random" + valueMap.get("first"));
    }

    @Test
    public void methodTest2() {
        System.out.println("methodTest2:" + Integer.toHexString(hashCode()));
    }

    @Test
    public void methodTest3() {
        System.out.println("methodTest3:" + Integer.toHexString(hashCode()));

        if (valueMap.get("random") < 50) {
            throw new RuntimeException("Crash simulate  in MethodTest3[" + Integer.toHexString(hashCode()) + "] (random =" + valueMap.get("random") + ")");
        }
    }

    public void methodDoNotTest() {
        System.out.println("methodTest2:" + Integer.toHexString(hashCode()));
    }

    @After
    public void methodAfter1() {
        System.out.println("methodAfter1:" + Integer.toHexString(hashCode()));
        if (valueMap.get("random") > 90) {
            throw new RuntimeException("Crash simulate resource unavailability in MethodAfter1[" + Integer.toHexString(hashCode()) + "] (random =" + valueMap.get("random") + ")");
        }
    }

    @After
    public void methodAfter2() {
        System.out.println("methodAfter2:" + Integer.toHexString(hashCode()));
        if (valueMap.get("random") < 20) {
            throw new RuntimeException("Crash simulate resource unavailability in MethodAfter1[" + Integer.toHexString(hashCode()) + "] (random =" + valueMap.get("random") + ")");
        }
    }
}
