package com.romanpulov.violetnotefx;

import com.romanpulov.violetnotefx.Model.Document;
import com.romanpulov.violetnotefx.Presentation.categorynotes.CategoryNotesStage;
import com.romanpulov.violetnotefx.Presentation.masterpass.MasterPassStage;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        MasterPassStage.MasterPassData data = new MasterPassStage.MasterPassData();
        MasterPassStage.showStage(data);
        log.debug("data = " + data);

        Document.getInstance().importPins("data\\\\pins_example.csv");
        CategoryNotesStage.showStage();
    }
}
