package dev.extrreme.logbook.utils.executable;

import java.util.concurrent.Callable;

/**
 * A simple interface that acts similar to a {@link Callable} and a {@link Executable} except it takes in a
 * parameter and returns a value
 * @param <T> the type of object the execute method should accept
 * @param <V> the type of object the execute method should return
 */
@FunctionalInterface
public interface ReturnExecutable<T,V> {
    V execute(T t);
}
