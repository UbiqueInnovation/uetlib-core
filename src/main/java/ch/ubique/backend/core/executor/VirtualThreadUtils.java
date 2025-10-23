package ch.ubique.backend.core.executor;

import ch.ubique.backend.core.utils.MdcUtils;

import com.pivovarit.function.ThrowingConsumer;
import com.pivovarit.function.ThrowingFunction;
import com.pivovarit.function.exception.WrappedException;

import org.apache.commons.lang3.exception.UncheckedException;
import org.apache.commons.lang3.exception.UncheckedInterruptedException;
import org.apache.commons.lang3.stream.Streams;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public final class VirtualThreadUtils {

    private VirtualThreadUtils() {}

    public static <T, V> List<V> mapAll(final Function<T, V> mapper, final Iterable<T> inputs) {
        return mapAll(mapper, Streams.of(inputs)).toList();
    }

    public static <T> void forEach(final Consumer<T> consumer, final Iterable<T> inputs) {
        forEach(consumer, Streams.of(inputs));
    }

    public static <T> void forEach(final Consumer<T> consumer, final Stream<T> inputs) {
        final var context = MDC.getCopyOfContextMap();
        final var tasks = inputs.map(callable(consumer, context)).toList();
        mapAll(tasks);
    }

    /**
     * Calls a throwing consumer for every input element.
     *
     * <p>If an exception is thrown inside a virtual, it is rethrown on the calling thread. Any
     * computations are skipped after the first exception has been thrown.
     */
    public static <T, E extends Exception> void throwingForEach(
            final ThrowingConsumer<T, E> throwingConsumer, final Stream<T> inputs) throws E {
        throwingMapAll(throwingConsumer.asFunction(), inputs.toList());
    }

    /**
     * Calls a throwing consumer for every input element.
     *
     * <p>If an exception is thrown inside a virtual, it is rethrown on the calling thread. Any
     * computations are skipped after the first exception has been thrown.
     */
    public static <T, E extends Exception> void throwingForEach(
            final ThrowingConsumer<T, E> throwingConsumer, final Iterable<T> inputs) throws E {
        throwingForEach(throwingConsumer, Streams.of(inputs));
    }

    public static <T, V> Stream<V> mapAll(final Function<T, V> mapper, final Stream<T> inputs) {
        final var tasks = inputs.map(callable(mapper)).toList();
        return mapAll(tasks);
    }

    public static <R> Stream<R> mapAll(final Collection<Callable<R>> tasks) {
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final var futures = executor.invokeAll(tasks);
            final var results = new ArrayList<R>();
            for (final var future : futures) {
                results.add(future.get());
            }
            return results.stream();
        }
        // This thread got interrupted while waiting for tasks to finish
        catch (final InterruptedException e) {
            // Set interrupt status again. This way the interrupt bubbles up and is not lost.
            Thread.currentThread().interrupt();
            throw new UncheckedInterruptedException(e);
        }
        // Some task threw an error
        catch (final ExecutionException e) {

            // Unwrap the ExecutionException. We want to get the exception that the task threw
            final var cause = e.getCause();

            // Errors should never be caught
            if (cause instanceof final Error error) {
                throw error;
            }

            // Easy case, just rethrow runtime exceptions
            if (cause instanceof final RuntimeException runtimeException) {
                throw runtimeException;
            }

            // Since the method signature does not have any checked exceptions, we have to rethrow
            // the cause as an unchecked exception.
            // This does not happen normally since the Callable<R> does not declare any check
            // exceptions. But it might still happen due to sneaky throws within the callable.
            throw new UncheckedException(e);
        }
    }

    /**
     * Calls a throwing function for every element in inputs and returns a collection of all
     * results.
     *
     * <p>If an exception is thrown inside a virtual, it is rethrown on the calling thread. Any
     * computations are skipped after the first exception has been thrown.
     */
    public static <T, V, E extends Exception> List<V> throwingMapAll(
            final ThrowingFunction<T, V, E> throwingMapper, final Iterable<T> inputs) throws E {
        final var exceptions = new ConcurrentLinkedDeque<E>();
        final var results =
                mapAll(
                        input -> {
                            if (!exceptions.isEmpty()) {
                                return null;
                            }
                            try {
                                return throwingMapper.uncheck().apply(input);
                            } catch (final WrappedException e) {
                                //noinspection unchecked
                                exceptions.add((E) e.getCause());
                                return null;
                            }
                        },
                        inputs);
        if (!exceptions.isEmpty()) {
            throw exceptions.getFirst();
        }
        return results;
    }

    private static <T, V> Function<T, Callable<V>> callable(final Function<T, V> mapper) {
        return input -> callable(mapper, input);
    }

    private static <T> Function<T, Callable<Integer>> callable(
            final Consumer<T> consumer, final Map<String, String> mdcContext) {
        return input -> callable(consumer, input, mdcContext);
    }

    private static <T> Callable<Integer> callable(
            final Consumer<T> consumer, final T input, final Map<String, String> mdcContext) {
        return () -> {
            MdcUtils.runWithContext(() -> consumer.accept(input), mdcContext);
            return 0;
        };
    }

    private static <T, V> Callable<V> callable(final Function<T, V> mapper, final T input) {
        final var context = MDC.getCopyOfContextMap();
        return () ->
                MdcUtils.runWithContext((Callable<V>) () -> mapper.apply(input), context).call();
    }
}
