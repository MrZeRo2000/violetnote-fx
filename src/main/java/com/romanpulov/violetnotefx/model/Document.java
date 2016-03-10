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

    {
        resetFileName();
    }

    public void resetFileName() {
        fileName.setValue(NEW_FILE_NAME);
    }

    private static Document ourInstance = new Document();
    private static final Logger log = LoggerFactory.getLogger(Document.class);

    private PassData passData;

    public PassData getPassData() {
        return passData;
    }
    public void setPassData(PassData passData) {
        this.passData = passData;
    }

    public void importPins(String fileName) {
        PinsDataReader pinsReader = new PinsDataReader();
        passData = null;
        try {
            passData = pinsReader.readStream(new FileInputStream(fileName));
        } catch (DataReadWriteException | FileNotFoundException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }

    public static Document getInstance() {
        return ourInstance;
    }

    private Document() {
    }
}
