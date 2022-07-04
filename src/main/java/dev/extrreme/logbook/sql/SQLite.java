package dev.extrreme.logbook.sql;

import dev.extrreme.logbook.utils.IgnoredResult;
import dev.extrreme.logbook.utils.StringUtility;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite implements SQL {
    private File dbFile;
    private SQLiteManager manager;

    public SQLite(String dbName, File parent) {
        try {
            System.out.println("Initializing SQL connection...");
            Class.forName("org.sqlite.JDBC");

            if (dbName.endsWith(".db")) {
                dbName = StringUtility.replaceLast(dbName, ".db", "");
            }

            dbFile = new File(parent, dbName + ".db");

            if (!dbFile.exists()){
                dbFile.createNewFile();
            }

            manager = new SQLiteManager(this);

            System.out.println("Successfully connected!");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC SQLite driver not found! Please find the"
                    + " version required for your OS and put it into the '/libs' folder!");
        } catch (IOException e) {
            System.out.println("File error: " + dbName.replace(".db", "") + ".db");
            e.printStackTrace();
        }
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
