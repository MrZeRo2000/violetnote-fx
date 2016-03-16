package com.romanpulov.violetnotefx;

import com.romanpulov.violetnotefx.Presentation.categorynotes.CategoryNotesModel;
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

    public void start(Stage stage) throws Exception {
        List<String> params = getParameters().getRaw();
        log.debug("params:" + params.toString());

        CategoryNotesStage categoryNotesStage = new CategoryNotesStage();
        CategoryNotesModel categoryNotesModel = categoryNotesStage.createStage();
        if (params.size() > 0) {
            categoryNotesModel.setLoadFileName(params.get(0));
        }
        categoryNotesStage.show();
    }
}
