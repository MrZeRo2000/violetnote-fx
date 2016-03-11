package com.romanpulov.violetnotefx.Model;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.PinsDataReader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by 4540 on 22.02.2016.
 */
public class Document {
    private final static String NEW_FILE_NAME = "New";
    private StringProperty fileName = new SimpleStringProperty();

    public StringProperty getFileName() {
        return fileName;
    }

    private String masterPass;

    public String getMasterPass() {
        return masterPass;
    }

    {
        resetFileName();
    }

    public void resetFileName() {
        masterPass = null;
        fileName.setValue(NEW_FILE_NAME);
    }

    public boolean isNewFile() {
        return fileName.equals(NEW_FILE_NAME);
    }

    public void setFile(String fileName, String masterPass) {
        this.fileName.setValue(fileName);
        this.masterPass = masterPass;
    }

    private static Document ourInstance = new Document();
    private static final Logger log = LoggerFactory.getLogger(Document.class);

    public static Document getInstance() {
        return ourInstance;
    }

    private Document() {
    }
}
