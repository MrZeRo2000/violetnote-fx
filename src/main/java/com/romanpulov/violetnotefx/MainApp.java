package com.romanpulov.violetnotefx;

import com.romanpulov.violetnotefx.Model.Document;
import com.romanpulov.violetnotefx.Presentation.categorynotes.CategoryNotesStage;
import com.romanpulov.violetnotefx.Presentation.masterpass.MasterPassStage;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public static void setupStageIcons(Stage stage) {
        stage.getIcons().addAll(
                new Image("images/Icon_16_16.png"),
                new Image("images/Icon_32_32.png")
        );
    }

    public void start(Stage stage) throws Exception {
        List<String> params = getParameters().getRaw();
        log.debug("params:" + params.toString());

        CategoryNotesStage.CategoryNotesData data = new CategoryNotesStage.CategoryNotesData();
        if (params.size() > 0) {
            data.setLoadFileName(params.get(0));

            /*
            File f = new File(params.get(0));
            if (f.exists()) {
                log.debug("File to process: " + f.getPath());
                String fileName = f.getName();
                int dotPos = fileName.lastIndexOf(".");
                if (dotPos > 0) {
                    String extension = fileName.substring(dotPos + 1).toUpperCase();
                    log.debug("extension = " + extension);
                }
            }
            */
        }

        CategoryNotesStage.showStage(data);
    }
}
