package org.lilbrocodes.conductor.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@SuppressWarnings({"CallToPrintStackTrace", "unused"})
public class ConfigUtils {
    public static Gson GSON = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().create();
    public static Logger LOGGER = LogManager.getLogger("Config Handler");

    public static <T> T getConfigObject(Class<T> configClass) {
        T object = null;
        try {
            object = configClass.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public static <T> T load(Path configFile, Class<T> configClass) {
        try {
            if (!Files.isDirectory(configFile.getParent())) {
                Files.createDirectories(configFile.getParent());
            }

            if (Files.isRegularFile(configFile)) {
                String json = Files.readString(configFile);
                T obj = GSON.fromJson(json, configClass);
                if (obj == null) {
                    LOGGER.error("Parsed object is null. Possible JSON syntax error in file: {}", configFile);
                    return null;
                }

                save(configFile, obj);
                return obj;
            }
        } catch (JsonSyntaxException e) {
            LOGGER.error("JSON syntax error while loading config! {} {}", configClass, e.getMessage());
            LOGGER.error("This error most often happens when you e.g. forget to put a comma between fields in JSON file. Check the file: {}", configFile.toAbsolutePath().normalize());
            return null;
        } catch (Exception e) {
            LOGGER.error("Couldn't load config! {}", configClass);
            e.printStackTrace();
        }

        try {
            T obj = getConfigObject(configClass);
            save(configFile, obj);
            return obj;
        } catch (Exception e) {
            LOGGER.error("Invalid config class! {}", configClass);
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T load(String json, Class<T> configClass) {
        try {
            if (json != null) {
                return GSON.fromJson(json, configClass);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load config! {}", configClass);
            e.printStackTrace();
        }

        return null;
    }

    public static void save(Path file, Object jsonObject) {
        try {
            if (!Files.isDirectory(file.getParent())) {
                Files.createDirectories(file.getParent());
            }

            Files.writeString(file, GSON.toJson(jsonObject), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            LOGGER.error("Couldn't save config! {}", jsonObject.getClass());
            e.printStackTrace();
        }
    }
}
