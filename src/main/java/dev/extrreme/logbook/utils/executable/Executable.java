package dev.extrreme.logbook.utils.executable;

import java.util.concurrent.Callable;

/**
 * A simple interface that acts similar to a {@link Callable} except it takes in a parameter instead of returning a
 * value
 * @param <T> type of object the execute method should accept
 */
@FunctionalInterface
public interface Executable<T> {
    void execute(T t);
}