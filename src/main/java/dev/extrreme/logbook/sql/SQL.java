package dev.extrreme.logbook.sql;

import java.sql.Connection;

public interface SQL {
    SQLManager getManager();
    Connection getConnection();
    boolean testConnection();
}
