package com.romanpulov.violetnotefx;

import com.romanpulov.violetnotefx.model.Document;
import com.romanpulov.violetnotefx.presentation.categorynotes.CategoryNotesStage;
import com.romanpulov.violetnotefx.presentation.categorynotes.CategoryNotesView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        Document.getInstance().importPins("data\\\\pins_example.csv");
        CategoryNotesStage.showStage();
    }
}
