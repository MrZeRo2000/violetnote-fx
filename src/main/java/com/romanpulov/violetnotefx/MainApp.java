package com.romanpulov.violetnotefx;

import com.romanpulov.violetnotefx.categoryname.CategoryNameStage;
import com.romanpulov.violetnotefx.categorynotes.CategoryNotesView;
import com.romanpulov.violetnotefx.core.injection.Binder;
import com.romanpulov.violetnotefx.masterpass.MasterPassModel;
import com.romanpulov.violetnotefx.masterpass.MasterPassView;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
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

        new AlertDialogs.ErrorAlertBuilder().setHeaderText("Header Text").buildAlert().showAndWait();

        //showCategoryNotes();

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
