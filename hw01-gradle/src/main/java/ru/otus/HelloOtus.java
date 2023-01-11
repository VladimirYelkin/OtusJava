/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ru.otus;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 *
 * To start the application:
 * ./gradlew build
 * java -jar ./L01-gradle/build/libs/gradle-hw01-0.2.jar
 *
 * To unzip the jar:
 * unzip -l hw01-gradle.jar
 * unzip -l gradle-hw01-0.2.jar
 *
 */
public class HelloOtus {
    public static void main(String... args) {
        List<Integer> example = IntStream.range(0,100).boxed().collect(Collectors.toList());
        var listes = Lists.partition(example,34);
        listes.forEach(System.out::println);
        System.out.println(Lists.reverse(example));
    }
}
