package com.nicc.ReactiveTutorial;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ReactiveTutorial {

    // provides a single string as the return type
    private Mono<String> testMono() {
        return Mono.just("Java")
                .log();
    }

    // provides multiple strings as the return type
    private Flux<String> testFlux() {
        return Flux.just("Java", "Python", "Scheme", "Scala")
                .log();
    }

    private Flux<String> testFluxIter() {
        List<String> lst = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));

        return Flux.fromIterable(lst)
                .log();
    }

    private Flux<String> testFluxMap() {
        List<String> lst = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));

        return Flux.fromIterable(lst)
                .map(str -> str.toLowerCase(Locale.ROOT));
    }

    public static void main(String[] args) {
        ReactiveTutorial reactiveTutorial = new ReactiveTutorial();
        // lambda
        reactiveTutorial.testMono()
                .subscribe(data -> System.out.println(data));
        // method reference
        reactiveTutorial.testFlux()
                .subscribe(System.out::println);
        // while the lambda and method references are both acceptable, we typically
        // want to use lambda if we want to do more than just call a method. on the
        // other hand, method reference is used if we just want to perform a method --
        // in this example, System.out.println.
        reactiveTutorial.testFluxIter()
                .subscribe(System.out::println);
        reactiveTutorial.testFluxMap()
                .subscribe(System.out::println);
    }
}
