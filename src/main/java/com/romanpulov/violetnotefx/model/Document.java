package com.romanpulov.violetnotefx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 4540 on 22.02.2016.
 */
public class Document {
    private final static String NEW_FILE_NAME = "New";
    private final StringProperty fileName = new SimpleStringProperty();

    public enum FileType {FT_NONE, FT_IMPORT, FT_VNF}

    public static final Map<String, FileType> FILE_TYPES = new HashMap<>();
    static
    {
        FILE_TYPES.put("csv", Document.FileType.FT_IMPORT);
        FILE_TYPES.put("vnf", Document.FileType.FT_VNF);
    }

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
        return fileName.getValue().equals(NEW_FILE_NAME);
    }

    public void setFile(String fileName, String masterPass) {
        this.fileName.setValue(fileName);
        this.masterPass = masterPass;
    }

    private static final Document ourInstance = new Document();

    public static Document getInstance() {
        return ourInstance;
    }

    private Document() {
    }
}
