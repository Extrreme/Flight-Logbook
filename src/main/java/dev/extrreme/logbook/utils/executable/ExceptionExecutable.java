package dev.extrreme.logbook.utils.executable;

public interface ExceptionExecutable<T, V extends Throwable> {
    void execute(T t) throws V;
}
