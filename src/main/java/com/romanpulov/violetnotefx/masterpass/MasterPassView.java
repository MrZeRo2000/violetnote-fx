package com.romanpulov.violetnotefx.masterpass;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * Created by rpulov on 23.01.2016.
 */
public class MasterPassView {
    public Parent getView() {
        FXMLLoader loader = new FXMLLoader();
        try {
            return (Parent) loader.load(getClass().getResourceAsStream("/fxml/masterpass.fxml"));
        } catch (IOException e) {
            return null;
        }
    }
}
