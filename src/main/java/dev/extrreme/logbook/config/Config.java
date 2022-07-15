package dev.extrreme.logbook.config;

import dev.extrreme.logbook.utils.FileUtility;
import dev.extrreme.logbook.utils.IgnoredResult;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public abstract class Config {

    private final File file;
    private final Properties properties;

    private static final String COMMENTS = "Logbook Settings";

    public Config(String fileName, File parent) {
        FileUtility.createIfNotExists(parent);

        file = FileUtility.initFile(fileName, ".properties", parent);

        properties = new Properties();
        applyDefaults(properties);

        if (!file.exists()) {
            try {
                IgnoredResult.ignore(file.createNewFile());
                FileUtility.writeFile(file, writer ->
                        properties.store(writer, COMMENTS)
                );
            } catch (IOException e) {
                System.out.println("An error occurred trying to write to file" + file.getAbsolutePath());
            }
        }
    }

    public abstract void applyDefaults(Properties properties);

    public boolean load() {
        return FileUtility.createIfNotExists(file) && FileUtility.readFile(file, properties::load);
    }

    public boolean save() {
        return FileUtility.createIfNotExists(file)
                && FileUtility.writeFile(file, writer -> properties.store(writer, COMMENTS));
    }

    public String getValue(String key) {
        return properties.getProperty(key);
    }

    public void setValue(String key, String value) {
        properties.setProperty(key, value);
    }
}
