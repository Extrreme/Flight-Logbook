package dev.extrreme.logbook.sql;

import java.util.List;
import java.util.Map;

public interface SQLManager {

    boolean createTable(String tableName, String[] columns, String[] types);
    boolean createTable(String tableName, String[] columns, String[] types, String extra);
    boolean dropTable(String tableName);
    boolean truncateTable(String tableName);
    boolean setRowInTable(String tableName, String keyName, String keyValue, Map<String, Object> vals);
    boolean setValInTable(String tableName, String keyName, String keyValue, String valueName, String value);
    boolean deleteRowInTable(String tableName, String keyName, String keyValue);
    Object getValInTable(String tableName, String keyName, String keyValue, String valueName);
    List<Map<Object, Object>> getRowsInTable(String tableName, String keyName, String keyValue, String... columns);
    List<Map<Object, Object>> getAllRowsInTable(String tableName, String... columns);
    List<Object> getColumnInTable(String tableName, String column);
    int getRowCount(String tableName);
    boolean execute(String statement);
    boolean execute(String statement, Map<Integer, Object> params);
}
