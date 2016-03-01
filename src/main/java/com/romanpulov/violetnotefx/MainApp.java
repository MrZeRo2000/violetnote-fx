package com.romanpulov.violetnotefx;

import com.romanpulov.violetnotefx.model.Document;
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

    public void showCategoryNotes() {
        CategoryNotesView categoryNotesView = new CategoryNotesView();
        Stage categoryNotesStage = new Stage();
        Scene categoryNotesScene = new Scene(categoryNotesView.getView());
        categoryNotesStage.setScene(categoryNotesScene);
        categoryNotesStage.show();
    }

    public void start(Stage stage) throws Exception {

        //CategoryNameStage.showStage();

        // prepare pins data

        Document.getInstance().importPins("data\\\\pins_example.csv");

        //new AlertDialogs.ErrorAlertBuilder().setContentText("Content").buildAlert().showAndWait();

        showCategoryNotes();

        /*
        MasterPassView masterPassView = new MasterPassView();

        Stage masterPassStage = new Stage();
        masterPassStage.setTitle("Master Password");
        Scene masterPassScene = new Scene(masterPassView.getView());
        MasterPassModel model = (MasterPassModel) masterPassView.getModelInstance();

        masterPassStage.setScene(masterPassScene);
        masterPassStage.setResizable(false);
        masterPassStage.initModality(Modality.APPLICATION_MODAL);
        log.debug("Show and wait");
        masterPassStage.showAndWait();
        log.debug("After wait");

        if (model.modelResult.getValue() == 1) {
            log.info("the password is " + model.passwordField_textProperty.getValue());


            CategoryNotesView categoryNotesView = new CategoryNotesView();
            Stage categoryNotesStage = new Stage();
            Scene categoryNotesScene = new Scene(categoryNotesView.getView());
            categoryNotesStage.setScene(categoryNotesScene);
            categoryNotesStage.show();
        }
        */
    }
}
