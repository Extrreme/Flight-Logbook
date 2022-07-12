package dev.extrreme.logbook.config;

import dev.extrreme.logbook.utils.IgnoredResult;
import dev.extrreme.logbook.utils.StringUtility;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public abstract class Config {

    private final File file;
    private final Properties properties;

    public Config(String fileName, File parent) {
        if (fileName.endsWith(".properties")) {
            fileName = StringUtility.replaceLast(fileName, ".properties", "");
        }

        file = new File(parent, fileName + ".properties");

        properties = new Properties();
        applyDefaults(properties);

        if (!file.exists()){
            try {
                IgnoredResult.ignore(file.createNewFile());
                FileWriter writer = new FileWriter(file);
                properties.store(writer, "Logbook Settings");
                writer.close();
            } catch (IOException e) {
                System.out.println("An error occurred trying to write to file" + file.getAbsolutePath());
            }
        }
    }

    public abstract void applyDefaults(Properties properties);

    public boolean load() {
        try {
            if (!file.exists()) {
                IgnoredResult.ignore(file.createNewFile());
            }
            FileReader reader = new FileReader(file);
            properties.load(reader);
            reader.close();
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred trying to read from file " + file.getAbsolutePath());
            return false;
        }
    }

    public boolean save() {
        try {
            if (!file.exists()) {
                IgnoredResult.ignore(file.createNewFile());
            }
            FileWriter writer = new FileWriter(file);
            properties.store(writer, "Logbook Settings");
            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred trying to write to file" + file.getAbsolutePath());
            return false;
        }
    }

    public String getValue(String key) {
        return properties.getProperty(key);
    }

    public void setValue(String key, String value) {
        properties.setProperty(key, value);
    }
}
