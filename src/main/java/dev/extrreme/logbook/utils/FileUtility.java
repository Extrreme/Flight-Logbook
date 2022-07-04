package dev.extrreme.logbook.utils;

public class FileUtility {
    /**
     * A string containing the filepath separator character
     */
    public static final String SEPARATOR = System.getProperty("file.separator");

    /**
     * A string containing the user appdata directory filepath
     */
    public static final String USER_APPDATA = System.getenv("APPDATA") + SEPARATOR;
}
