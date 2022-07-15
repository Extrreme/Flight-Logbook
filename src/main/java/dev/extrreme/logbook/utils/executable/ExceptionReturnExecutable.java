package dev.extrreme.logbook.utils.executable;

public interface ExceptionReturnExecutable<T, V, R extends Throwable> {
    V execute(T t) throws R;
}
