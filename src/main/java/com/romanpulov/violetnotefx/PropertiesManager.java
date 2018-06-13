package com.romanpulov.violetnotefx;

import java.io.*;
import java.util.Properties;

/**
 * Created by 4540 on 10.03.2016.
 */
public class PropertiesManager {
    private static final String PROPERTIES_FILE_NAME;
    private static final String FILE_NAME = "config.properties";
    public static final String DOCUMENT_FILE_NAME = "document_file_name";
    public static final String LOG_LEVEL = "log_level";

    private final Properties properties = new Properties();
    private boolean isModified = false;

    public boolean isModified() {
        return isModified;
    }

    static {
        PROPERTIES_FILE_NAME = PropertiesManager.class.getProtectionDomain().getCodeSource().getLocation().getPath() + FILE_NAME;
    }

    private static final PropertiesManager ourInstance = new PropertiesManager();

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value){
        String oldValue = properties.getProperty(key);
        if (((oldValue != null) && !(oldValue.equals(value))) || ((value != null) && !(value.equals(oldValue)))) {
            properties.setProperty(key, value);
            isModified = true;
        }
    }

    /**
     * Clears properties
     */
    public void clear(){
        properties.clear();
    }

    public void load() {
        isModified = false;
        try (InputStream  input = new FileInputStream(PROPERTIES_FILE_NAME)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (isModified) {
            try (OutputStream output = new FileOutputStream(PROPERTIES_FILE_NAME)) {
                properties.store(output, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean deletePropertiesFile() {
        File f = new File(PROPERTIES_FILE_NAME);
        return f.exists() && f.delete();
    }

    public static PropertiesManager getInstance() {
        return ourInstance;
    }
}
