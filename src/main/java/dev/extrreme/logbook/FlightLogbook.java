package dev.extrreme.logbook;

import dev.extrreme.logbook.manager.AircraftManager;
import dev.extrreme.logbook.manager.FlightManager;
import dev.extrreme.logbook.scheduling.Scheduler;
import dev.extrreme.logbook.utils.FileUtility;
import dev.extrreme.logbook.sql.SQL;
import dev.extrreme.logbook.sql.SQLite;
import dev.extrreme.logbook.ui.Window;

import java.io.File;

public class FlightLogbook {
    private static File workingDir;

    private static SQL sql;
    private static Window window;

    public static void main(String[] args) {
        if (!initWorkingDir() || !initSQL()) {
            return;
        }

        Scheduler.getInstance();

        if (!AircraftManager.init() || !FlightManager.init()) {
            return;
        }

        window = new Window();
    }

    public static void close() {
        Scheduler.shutdown();
        System.exit(0);
    }

    private static boolean initWorkingDir() {
        workingDir = new File(FileUtility.USER_APPDATA + "FlightLogbook" + FileUtility.SEPARATOR);
        return workingDir.exists() || workingDir.mkdirs();
    }

    private static boolean initSQL() {
        sql = new SQLite("logbook", workingDir);
        return sql.testConnection();
    }

    public static SQL getSQL() {
        return sql;
    }

    public static Window getWindow() {
        return window;
    }
}
