package com.softwareconfidence.primes.domain;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.googlecode.totallylazy.numbers.Numbers.lessThanOrEqualTo;
import static com.googlecode.totallylazy.numbers.Numbers.primes;

public class PrimesCalculator {
    private final ExecutorService executor;

    public PrimesCalculator(ExecutorService executor) {
        this.executor = executor;
    }

    public Future<List<Number>> submit(Long upto) {
        return executor.submit(task(upto));
    }

    private Callable<List<Number>> task(Long upto) {
        return () -> primes().takeWhile(lessThanOrEqualTo(upto)).toList();
    }
}
