package dev.extrreme.logbook;

import dev.extrreme.logbook.config.Config;
import dev.extrreme.logbook.config.ConfigKeys;
import dev.extrreme.logbook.manager.AircraftManager;
import dev.extrreme.logbook.manager.FlightManager;
import dev.extrreme.logbook.scheduling.Scheduler;
import dev.extrreme.logbook.utils.FileUtility;
import dev.extrreme.logbook.sql.SQL;
import dev.extrreme.logbook.sql.SQLite;
import dev.extrreme.logbook.ui.LogbookViewer;

import java.io.File;
import java.util.Properties;

public class FlightLogbook {
    private static File workingDir;

    private static Config config;
    private static SQL sql;
    private static LogbookViewer window;

    public static void main(String[] args) {
        if (!initWorkingDir() || !initConfig() || !initSQL()) {
            return;
        }

        Scheduler.getInstance();

        if (!AircraftManager.init() || !FlightManager.init()) {
            return;
        }

        window = new LogbookViewer();
    }

    public static void close() {
        config.save();
        Scheduler.shutdown();
        System.exit(0);
    }

    private static boolean initWorkingDir() {
        workingDir = new File(FileUtility.USER_APPDATA + "FlightLogbook" + FileUtility.SEPARATOR);
        return workingDir.exists() || workingDir.mkdirs();
    }

    private static boolean initConfig() {
        config = new Config("config", workingDir) {
            @Override
            public void applyDefaults(Properties defaults) {
                defaults.setProperty(ConfigKeys.DARK_MODE, "false");
            }
        };
        return config.load();
    }

    private static boolean initSQL() {
        sql = new SQLite("logbook", workingDir);
        return sql.testConnection();
    }

    public static File getWorkingDir() {
        return workingDir;
    }

    public static Config getConfig() {
        return config;
    }

    public static SQL getSQL() {
        return sql;
    }

    public static LogbookViewer getWindow() {
        return window;
    }
}
