package org.example.concurrency.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class IntMultiplication implements Callable<Integer> {

    private final int value;

    @Override
    public Integer call() {
        int result = value * ThreadLocalRandom.current().nextInt(1, 100);
        log.info("Product {} was calculated in thread {}", result, Thread.currentThread().getName());

        return result;
    }
}