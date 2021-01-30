package io.happyharbor.cardmarket.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class JavaCompletableFuturesSequencer {

    public <T> CompletableFuture<List<T>> sequence(final List<CompletableFuture<T>> futures) {
        CompletableFuture<List<T>> completableFuture = new CompletableFuture<>();

        final int total = futures.size();
        final AtomicInteger counter = new AtomicInteger();

        final List<T> results = new ArrayList<>(futures.size());

        futures.forEach(f -> f.whenComplete((ok, error) -> {
            if (error == null) {
                synchronized (results) {
                    results.add(ok);
                }
            } else {
                log.debug("Future {} was not completed", f);
            }

            int totalDone = counter.incrementAndGet();
            if (totalDone == total) {
                // TODO Could also fail result when non of the futures completed successfully.
                completableFuture.complete(results);
            }
        }));

        return completableFuture;
    }
}
