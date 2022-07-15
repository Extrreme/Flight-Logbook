package dev.extrreme.logbook.sql;

import dev.extrreme.logbook.utils.FileUtility;
import dev.extrreme.logbook.utils.StringUtility;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite implements SQL {
    private final File dbFile;
    private final SQLiteManager manager;

    public SQLite(String dbName, File parent) {
        System.out.println("Initializing SQL connection...");

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC SQLite driver not found! Please find the"
                    + " version required for your OS and put it into the '/libs' folder!");
        }

        if (dbName.endsWith(".db")) {
            dbName = StringUtility.replaceLast(dbName, ".db", "");
        }

        dbFile = new File(parent, dbName + ".db");
        FileUtility.createIfNotExists(dbFile);

        manager = new SQLiteManager(this);

        System.out.println("Successfully connected!");
    }

    @Override
    public SQLiteManager getManager() {
        return this.manager;
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + this.dbFile);
        } catch (SQLException e) {
            System.out.println("Error connecting to SQL database, please check"
                    + " your credentials and try again!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean testConnection() {
        Connection conn = getConnection();
        if (conn == null) {
            return false;
        }
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error closing connection to SQL database!");
            return false;
        }

        return true;
    }
}
