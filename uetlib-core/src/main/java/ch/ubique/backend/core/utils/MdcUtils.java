package ch.ubique.backend.core.utils;

import ch.ubique.backend.core.mdc.MdcForkJoinPool;

import com.pivovarit.function.ThrowingConsumer;
import com.pivovarit.function.ThrowingFunction;
import com.pivovarit.function.ThrowingSupplier;

import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class MdcUtils {

    private MdcUtils() {}

    public static void runWithContext(Runnable runnable, final Map<String, String> context) {
        if (context == null) {
            runnable.run();
            return;
        }

        final var previous = MDC.getCopyOfContextMap();
        MDC.setContextMap(context);
        runnable.run();
        MDC.clear();
        if (previous != null) {
            MDC.setContextMap(previous);
        }
    }

    /**
     * Use this to submit a function that is called inside a parallel {@link Stream#map} to the
     * {@link MdcForkJoinPool#mdcCommonPool()}. Use as follows:
     *
     * <pre>{@code
     * final var mdcContext = MDC.getCopyOfContextMap();
     * final var result = list.parallelStream()
     *         .map(MdcUtils.mdcCommonPoolSubmitUnchecked(function, mdcContext))
     *         .toList();
     * }</pre>
     *
     * @param function function to be executed with the given MDC context
     * @param newContext MDC context that is set in the executing thread
     */
    public static <T, R> @NotNull Function<T, R> mdcCommonPoolSubmitUnchecked(
            final Function<T, R> function, final Map<String, String> newContext) {
        return ThrowingFunction.unchecked(
                arg ->
                        MdcForkJoinPool.mdcCommonPool()
                                .submit(() -> function.apply(arg), newContext)
                                .get());
    }

    /**
     * Use this to submit a consumer that is called inside a parallel {@link Stream#forEach} to the
     * {@link MdcForkJoinPool#mdcCommonPool()}. Use as follows:
     *
     * <pre>{@code
     * final var mdcContext = MDC.getCopyOfContextMap();
     * list.parallelStream()
     *         .forEach(MdcUtils.mdcCommonPoolSubmitUnchecked(consumer, mdcContext));
     * }</pre>
     *
     * @param consumer consumer to be executed with the given MDC context
     * @param newContext MDC context that is set in the executing thread
     */
    public static <T> @NotNull Consumer<T> mdcCommonPoolSubmitUnchecked(
            final Consumer<T> consumer, final Map<String, String> newContext) {
        return ThrowingConsumer.unchecked(
                arg ->
                        MdcForkJoinPool.mdcCommonPool()
                                .submit(() -> consumer.accept(arg), newContext)
                                .get());
    }

    /**
     * Use this to submit a supplier to the {@link MdcForkJoinPool#mdcCommonPool()}. Use as follows:
     *
     * <pre>{@code
     * final var mdcContext = MDC.getCopyOfContextMap();
     * final var result = MdcUtils.mdcCommonPoolSubmitUnchecked(supplier, newContext);
     * }</pre>
     *
     * @param supplier supplier to be executed with the given MDC context
     * @param newContext MDC context that is set in the executing thread
     */
    public static <T> @NotNull Supplier<T> mdcCommonPoolSubmitUnchecked(
            final Supplier<T> supplier, final Map<String, String> newContext) {
        return ThrowingSupplier.unchecked(
                () -> MdcForkJoinPool.mdcCommonPool().submit(supplier::get, newContext).get());
    }
}
