package dev.extrreme.logbook.sql;

import dev.extrreme.logbook.utils.executable.ExceptionReturnExecutable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SQLManager {
    interface SQLExecutable<T> extends ExceptionReturnExecutable<Connection, T, SQLException> {}

    boolean createTable(String tableName, String[] columns, String[] types);
    boolean createTable(String tableName, String[] columns, String[] types, String extra);
    boolean dropTable(String tableName);
    boolean truncateTable(String tableName);
    boolean setRowInTable(String tableName, String keyName, String keyValue, Map<String, Object> vals);
    boolean setValInTable(String tableName, String keyName, String keyValue, String valueName, String value);
    boolean deleteRowInTable(String tableName, String keyName, String keyValue);
    Object getValInTable(String tableName, String keyName, String keyValue, String valueName);
    List<Map<Object, Object>> getRowsInTable(String tableName, String keyName, String keyValue, String... columns);
    List<Map<Object, Object>> getRowsInTable(String tableName, String keyName, String keyValue, String[] columns, String extra);
    List<Map<Object, Object>> getAllRowsInTable(String tableName, String... columns);
    List<Map<Object, Object>> getAllRowsInTable(String tableName, String[] columns, String extra);
    List<Object> getColumnInTable(String tableName, String column);
    List<Object> getColumnInTable(String tableName, String column, String extra);
    int getRowCount(String tableName);
    boolean execute(String statement);
    boolean execute(String statement, Map<Integer, Object> params);
}
