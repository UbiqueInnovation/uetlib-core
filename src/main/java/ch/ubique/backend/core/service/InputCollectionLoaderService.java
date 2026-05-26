package ch.ubique.backend.core.service;

import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.function.Function;

public interface InputCollectionLoaderService<T> extends InputDataLoaderService<Collection<T>> {

    default <R> InputCollectionLoaderService<R> mapCollection(final Function<T, R> mapper) {
        return new InputCollectionLoaderServiceMapper<>(this, mapper);
    }
}
