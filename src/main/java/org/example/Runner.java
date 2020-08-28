package org.example;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.concurrency.callable.IntAddition;
import org.example.concurrency.callable.IntMultiplication;

@Slf4j
public class Runner {

    private static final int THREAD_NUMBER = 2;

    @SneakyThrows
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUMBER);

        int initialValue = ThreadLocalRandom.current().nextInt(1, 1000);
        List<Future<Integer>> futures = executorService.invokeAll(List.of(new IntAddition(initialValue), new IntMultiplication(initialValue)));

        List<Integer> integers = getResult(futures);
        log.info("Result: {}", integers);

        shutdownExecutorService(executorService);
    }

    private static List<Integer> getResult(List<Future<Integer>> futures) {
        return futures.stream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException exception) {
                throw new RuntimeException(exception);
            }
        }).collect(Collectors.toList());
    }

    private static void shutdownExecutorService(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}