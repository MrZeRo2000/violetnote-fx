package com.romanpulov.violetnotefx.masterpass;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Created by rpulov on 23.01.2016.
 */
public class MasterPassView {
    private static final Logger log = LoggerFactory.getLogger(MasterPassView.class);

    public Parent getView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/masterpass.fxml"));

        try {
            //return (Parent) loader.load(getClass().getResourceAsStream("/fxml/masterpass.fxml"));
            Parent view = loader.load();
            return view ;
        } catch (IOException e) {
            return null;
        }
    }
}
