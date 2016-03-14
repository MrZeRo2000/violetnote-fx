package com.romanpulov.violetnotefx;

import com.romanpulov.violetnotefx.Presentation.categorynotes.CategoryNotesStage;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        }

        (new CategoryNotesStage(data)).show();
    }
}
