package com.romanpulov.violetnotefx;

import com.romanpulov.violetnotefx.masterpass.MasterPassView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.romanpulov.violetnotecore.Model.Model1;

public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {

        MasterPassView masterPassView = new MasterPassView();
        Stage masterPassStage = new Stage();
        masterPassStage.setTitle("Master Password");
        Scene masterPassScene = new Scene(masterPassView.getView());
        masterPassStage.setScene(masterPassScene);
        masterPassStage.setResizable(false);
        masterPassStage.initModality(Modality.APPLICATION_MODAL);
        log.debug("Show and wait");
        masterPassStage.showAndWait();
        log.debug("After wait");

        log.info("Starting Hello JavaFX and Maven demonstration application");

        String fxmlFile = "/fxml/hello.fxml";
        log.debug("Loading FXML for main view from: {}", fxmlFile);
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

        log.debug("Showing JFX scene");
        Scene scene = new Scene(rootNode, 400, 200);
        scene.getStylesheets().add("/styles/styles.css");

        stage.setTitle("Hello JavaFX and Maven");
        stage.setScene(scene);
        stage.show();



    }
}
