package dev.extrreme.logbook.utils;

import dev.extrreme.logbook.utils.executable.ExceptionExecutable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtility {
    /**
     * A string containing the filepath separator character
     */
    public static final String SEPARATOR = System.getProperty("file.separator");

    /**
     * A string containing the user appdata directory filepath
     */
    public static final String USER_APPDATA = System.getenv("APPDATA") + SEPARATOR;

    public interface FileExecutable<T> extends ExceptionExecutable<T, IOException> {}

    /**
     * Form a full file name with the given name and extension, intended use case is when you are not sure if the file
     * name already contains the extension
     * @param fileName the name for the file to have, as a string (e.g., "config")
     * @param fileExtension the extension for the file to have, as a string (e.g., ".properties")
     * @return The full file name, as a string
     */
    public static String createFileName(@NotNull String fileName, @NotNull String fileExtension) {
        if (!fileExtension.startsWith(".")) {
            fileExtension = ".".concat(fileExtension);
        }
        if (fileName.endsWith(fileExtension)) {
            fileName = StringUtility.replaceLast(fileName, fileExtension, "");
        }

        return fileName.concat(fileExtension);
    }

    /**
     * Initialize a new {@link File} object
     * @param fileName the name for the file to have, as a string (e.g., "config")
     * @param fileExtension the extension for the file to have, as a string (e.g., ".properties")
     * @param parent the parent {@link File directory}
     * @return the new file object with the specified parent and name as returned by
     * {@link #createFileName(String, String)}
     */
    public static File initFile(String fileName, String fileExtension, File parent) {
        return new File(parent, createFileName(fileName, fileExtension));
    }

    /**
     * Create the specified file if it does not exist
     * @param file the file to create if it does not exist
     * @return TRUE if the file was successfully created or already exists, FALSE otherwise
     */
    public static boolean createIfNotExists(@NotNull File file) {
        if (file.exists()) {
            return true;
        }

        File parent = file.getParentFile();

        try {
            if (parent != null && parent.isDirectory() && !parent.exists()) {
                IgnoredResult.ignore(parent.mkdirs());
            }

            return file.exists() || (file.isDirectory() ? file.mkdirs() : file.createNewFile());
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Create the specified file if it does not exist
     * @param fileName the name for the file to have, as a string (e.g., "config")
     * @param fileExtension the extension for the file to have, as a string (e.g., ".properties")
     * @param parent the parent {@link File directory}
     * @return TRUE if the file was successfully created or already exists, FALSE otherwise
     */
    public static boolean createIfNotExists(String fileName, String fileExtension, File parent) {
        return createIfNotExists(initFile(fileName, fileExtension, parent));
    }

    /**
     * Perform the specified executable with an open {@link FileReader}
     * @param file the file to read from
     * @param executable the executable to be performed on the created file reader
     * @return TRUE if the executable executed without any exceptions, FALSE otherwise. It should be noted that this
     * method may return false and the executable ran successfully
     */
    public static boolean readFile(@NotNull File file, @NotNull FileExecutable<FileReader> executable) {
        try {
            FileReader reader = new FileReader(file);
            executable.execute(reader);
            reader.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Perform the specified executable with an open {@link FileWriter}
     * @param file the file to write to
     * @param executable the executable to be performed on the created file writer
     * @return TRUE if the executable executed without any exceptions, FALSE otherwise. It should be noted that this
     * method may return false and the executable ran successfully
     */
    public static boolean writeFile(@NotNull File file, @NotNull FileExecutable<FileWriter> executable) {
        try {
            FileWriter writer = new FileWriter(file);
            executable.execute(writer);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
