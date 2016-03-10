package com.romanpulov.violetnotefx;

import java.io.*;
import java.util.Properties;

/**
 * Created by 4540 on 10.03.2016.
 */
public class PropertiesManager {
    private static final String propertiesFileName;
    private static final String FILE_NAME = "config.properties";

    private final Properties properties = new Properties();

    public Properties getProperties() {
        return properties;
    }

    static {
        propertiesFileName = PropertiesManager.class.getProtectionDomain().getCodeSource().getLocation().getPath() + FILE_NAME;
    }

    private static PropertiesManager ourInstance = new PropertiesManager();

    public void load() {
        InputStream input = null;
        try {
            input = new FileInputStream(propertiesFileName);
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void save() {
        OutputStream output = null;
        try {
            output = new FileOutputStream(propertiesFileName);
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.flush();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean deletePropertiesFile() {
        File f = new File(propertiesFileName);
        return f.exists() && f.delete();
    }

    public static PropertiesManager getInstance() {
        return ourInstance;
    }
}
