package com.softwareconfidence.primes.domain;

import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

public class DomainModule implements ApplicationScopedModule, RequestScopedModule {
    @Override
    public Container addPerApplicationObjects(Container container) throws Exception {
        return container
                .addActivator(ExecutorService.class, ExecutorServiceActivator.class)
                .add(JobStore.class);
    }

    @Override
    public Container addPerRequestObjects(Container container) throws Exception {
        return container.add(PrimesCalculator.class);
    }

    public static final class ExecutorServiceActivator implements Callable<ExecutorService>, Closeable {

        private ForkJoinPool service;

        @Override
        public ExecutorService call() throws Exception {
            service = new ForkJoinPool();
            return service;
        }

        @Override
        public void close() throws IOException {
            if(service != null) {
                try {
                    service.shutdown();
                    service.awaitTermination(5, SECONDS);
                }
                catch (InterruptedException e) {
                    System.err.println("tasks interrupted");
                }
                finally {
                    service.shutdownNow();
                }
            }
        }
    }
}
