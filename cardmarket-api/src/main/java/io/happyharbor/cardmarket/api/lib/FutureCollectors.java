package io.happyharbor.cardmarket.api.lib;

import com.spotify.futures.CompletableFutures;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FutureCollectors {

    private FutureCollectors() {
    }

    /**
     * Collector that collects Completable Futures of T to aCompletable Futures of List of T
     * @param <T> the payload of the Completable Future
     * @return a list of Completable Futures of T
     */
    public static <T> Collector<CompletableFuture<T>, ?, CompletableFuture<List<T>>> sequenceCollector() {
        return Collectors.collectingAndThen(Collectors.toList(), com -> {
            CompletableFuture<List<T>> result = CompletableFuture.allOf(com.toArray(new CompletableFuture<?>[0]))
                    .thenApply(v -> com.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList())
                    );

            com.forEach(f -> f.whenComplete((t, ex) -> {
                if (ex != null) {
                    result.completeExceptionally(ex);
                }
            }));

            return result;
        });
    }

    /**
     * Collector that collects Completable Futures of T to aCompletable Futures of Stream of T
     * @param <T> the payload of the Completable Future
     * @return a Completable Future of Stream of T
     */
    public static <T> Collector<CompletableFuture<T>, ?, CompletableFuture<Stream<T>>> sequenceStreamCollector() {
        return Collectors.collectingAndThen(Collectors.toList(), com -> {
            CompletableFuture<Stream<T>> result = CompletableFuture.allOf(com.toArray(new CompletableFuture<?>[0]))
                    .thenApply(v -> com.stream()
                            .map(CompletableFuture::join)
                    );

            com.forEach(f -> f.whenComplete((t, ex) -> {
                if (ex != null) {
                    result.completeExceptionally(ex);
                }
            }));

            return result;
        });
    }

    /**
     * Collector that collects Completable Futures of T to a Completable Futures of List of T.
     * The Completable Future will not stop on any error and complete normally.
     * @param defaultValueFunction the default value
     * @param <T> the payload of the Completable Future
     * @return a list of Completable Futures of T
     */
    public static <T> Collector<CompletableFuture<T>, ?, CompletableFuture<List<T>>> sequenceNoFailCollector(
            final Function<Throwable, ? extends T> defaultValueFunction) {
        return Collectors.collectingAndThen(Collectors.toList(), futures -> CompletableFutures.successfulAsList(futures, defaultValueFunction));
    }
}
