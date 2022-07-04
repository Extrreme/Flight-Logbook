package dev.extrreme.logbook.utils.obj;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A copy of {@link ReturnExecutable} however it only accepts a {@link Connection connection} and throws a
 * {@link SQLException} to allow execution of SQL tasks
 * @param <T> the type of object the execute method should return
 */
@FunctionalInterface
public interface SQLExecutable<T> {
    T execute(Connection conn) throws SQLException;
}