package ch.ubique.backend.core.mdc;

import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A {@link ForkJoinPool} that inherits MDC contexts from the thread that queues a task.
 *
 * @author Gili Tzabari (<a href="https://stackoverflow.com/a/36075149">StackOverflow Answer</a>)
 * @author Ben L. (<a href="https://stackoverflow.com/a/50820315">StackOverflow Answer</a>)
 */
public final class MdcForkJoinPool extends ForkJoinPool {

    private static final MdcForkJoinPool MDC_COMMON_POOL = new MdcForkJoinPool(64);
    /**
     * Creates a new MdcForkJoinPool.
     *
     * @param parallelism the parallelism level. For default value, use {@link
     *     java.lang.Runtime#availableProcessors}.
     * @param factory the factory for creating new threads. For default value, use {@link
     *     #defaultForkJoinWorkerThreadFactory}.
     * @param handler the handler for internal worker threads that terminate due to unrecoverable
     *     errors encountered while executing tasks. For default value, use {@code null}.
     * @param asyncMode if true, establishes local first-in-first-out scheduling mode for forked
     *     tasks that are never joined. This mode may be more appropriate than default locally
     *     stack-based mode in applications in which worker threads only process event-style
     *     asynchronous tasks. For default value, use {@code false}.
     * @throws IllegalArgumentException if parallelism less than or equal to zero, or greater than
     *     implementation limit
     * @throws NullPointerException if the factory is null
     * @throws SecurityException if a security manager exists and the caller is not permitted to
     *     modify threads because it does not hold {@link java.lang.RuntimePermission}{@code
     *     ("modifyThread")}
     */
    public MdcForkJoinPool(
            final int parallelism,
            final ForkJoinWorkerThreadFactory factory,
            final UncaughtExceptionHandler handler,
            final boolean asyncMode) {
        super(parallelism, factory, handler, asyncMode);
    }

    public MdcForkJoinPool(final int parallelism) {
        this(parallelism, defaultForkJoinWorkerThreadFactory, null, false);
    }

    public static MdcForkJoinPool mdcCommonPool() {
        return MDC_COMMON_POOL;
    }

    @Override
    public void execute(final ForkJoinTask<?> task) {
        // See http://stackoverflow.com/a/19329668/14731
        super.execute(wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public void execute(final Runnable task) {
        // See http://stackoverflow.com/a/19329668/14731
        super.execute(wrap(task, MDC.getCopyOfContextMap()));
    }

    private <T> ForkJoinTask<T> wrap(
            final ForkJoinTask<T> task, final Map<String, String> newContext) {
        return new ForkJoinTask<T>() {
            /** If non-null, overrides the value returned by the underlying task. */
            private final AtomicReference<T> override = new AtomicReference<>();

            @Override
            public T getRawResult() {
                final var result = override.get();
                if (result != null) {
                    return result;
                }
                return task.getRawResult();
            }

            @Override
            protected void setRawResult(final T value) {
                override.set(value);
            }

            @Override
            protected boolean exec() {
                // According to ForkJoinTask.fork() "it is a usage error to fork a task more than
                // once unless it has completed
                // and been reinitialized". We therefore assume that this method does not have to be
                // thread-safe.
                final var oldContext = beforeExecution(newContext);
                try {
                    task.invoke();
                    return true;
                } finally {
                    afterExecution(oldContext);
                }
            }
        };
    }

    private Runnable wrap(final Runnable task, final Map<String, String> newContext) {
        return () -> {
            final var oldContext = beforeExecution(newContext);
            try {
                task.run();
            } finally {
                afterExecution(oldContext);
            }
        };
    }

    /**
     * Invoked before running a task.
     *
     * @param newValue the new MDC context
     * @return the old MDC context
     */
    private Map<String, String> beforeExecution(final Map<String, String> newValue) {
        final var previous = MDC.getCopyOfContextMap();
        if (newValue == null) {
            MDC.clear();
        } else {
            MDC.setContextMap(newValue);
        }
        return previous;
    }

    /**
     * Invoked after running a task.
     *
     * @param oldValue the old MDC context
     */
    private void afterExecution(final Map<String, String> oldValue) {
        if (oldValue == null) {
            MDC.clear();
        } else {
            MDC.setContextMap(oldValue);
        }
    }

    @Override
    public <T> ForkJoinTask<T> submit(final ForkJoinTask<T> task) {
        return super.submit(wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public <T> @NotNull ForkJoinTask<T> submit(final Callable<T> task) {
        return super.submit(wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public <T> @NotNull ForkJoinTask<T> submit(final Runnable task, final T result) {
        return super.submit(wrap(task, MDC.getCopyOfContextMap()), result);
    }

    @Override
    public @NotNull ForkJoinTask<?> submit(final Runnable task) {
        return super.submit(wrap(task, MDC.getCopyOfContextMap()));
    }

    public <T> ForkJoinTask<T> submit(
            final ForkJoinTask<T> task, final Map<String, String> newContext) {
        return super.submit(wrap(task, newContext));
    }

    public <T> @NotNull ForkJoinTask<T> submit(
            final Callable<T> task, final Map<String, String> newContext) {
        return super.submit(wrap(task, newContext));
    }

    public <T> @NotNull ForkJoinTask<T> submit(
            final Runnable task, final T result, final Map<String, String> newContext) {
        return super.submit(wrap(task, newContext), result);
    }

    public @NotNull ForkJoinTask<?> submit(
            final Runnable task, final Map<String, String> newContext) {
        return super.submit(wrap(task, newContext));
    }

    private <T> Callable<T> wrap(final Callable<T> task, final Map<String, String> newContext) {
        return () -> {
            final var oldContext = beforeExecution(newContext);
            try {
                return task.call();
            } finally {
                afterExecution(oldContext);
            }
        };
    }
}
