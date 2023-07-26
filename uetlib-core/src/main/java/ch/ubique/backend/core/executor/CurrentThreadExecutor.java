package ch.ubique.backend.core.executor;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Executor that executes in the current thread. Useful for unit tests or in single-threaded
 * settings
 */
public class CurrentThreadExecutor implements ExecutorService {

    @Override
    public void execute(final Runnable command) {
        command.run();
    }

    @Override
    public void shutdown() {}

    @Override
    public List<Runnable> shutdownNow() {
        return Collections.emptyList();
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(final long timeout, @NotNull final TimeUnit unit) {
        return false;
    }

    @Override
    public <T> Future<T> submit(@NotNull final Callable<T> task) {
        final var f = new FutureTask<>(task);
        f.run();
        return f;
    }

    @Override
    public <T> Future<T> submit(@NotNull final Runnable task, final T result) {
        final var f = new FutureTask<>(task, result);
        f.run();
        return f;
    }

    @Override
    public Future<?> submit(@NotNull final Runnable task) {
        final FutureTask<?> f = new FutureTask<>(task, null);
        f.run();
        return f;
    }

    @Override
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) {
        return tasks.stream().map(this::submit).toList();
    }

    @Override
    public <T> List<Future<T>> invokeAll(
            final Collection<? extends Callable<T>> tasks,
            final long timeout,
            @NotNull final TimeUnit unit) {
        return tasks.stream().map(this::submit).toList();
    }

    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException {
        return tasks.stream().map(this::submit).findFirst().orElseThrow().get();
    }

    @Override
    public <T> T invokeAny(
            final Collection<? extends Callable<T>> tasks,
            final long timeout,
            @NotNull final TimeUnit unit)
            throws InterruptedException, ExecutionException {
        return tasks.stream().map(this::submit).findFirst().orElseThrow().get();
    }
}
