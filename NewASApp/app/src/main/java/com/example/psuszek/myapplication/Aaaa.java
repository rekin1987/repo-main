package com.example.psuszek.myapplication;

import android.util.Log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by psuszek on 2017-07-26.
 */

public class Aaaa {

    public enum Sex {
        MALE, FEMALE
    }

    public class Person {

        String name;
        LocalDate birthday;
        Sex gender;
        String emailAddress;
        int age;

        public int getAge() {
            // ...
            return age;
        }

        public void printPerson() {
            // ...
        }
    }

    interface CheckPerson {
        boolean test(Person p);
    }


    void aa() {
        Runnable runnable = () -> {

        };

        new Runnable() {
            @Override
            public void run() {

            }
        };
    }

    void bb() {
        List<Person> roster = new ArrayList<>();
        printPersons(roster,
                p -> p.age > 10

        );

        processElements(roster,
                person -> person.age > 1,
                person -> person.emailAddress,
                strEmail -> Log.d("AAA", strEmail)
                );
    }

    public static void printPersons(List<Person> roster, CheckPerson tester) {
        for (Person p : roster) {
            if (tester.test(p)) {
                p.printPerson();
            }
        }
    }

    public static <X, Y> void processElements(
            Iterable<X> source,
            Predicate<X> tester,
            Function<X, Y> mapper,
            Consumer<Y> block) {
        for (X p : source) {
            if (tester.test(p)) {
                Y data = mapper.apply(p);
                block.accept(data);
            }
        }
    }

}
