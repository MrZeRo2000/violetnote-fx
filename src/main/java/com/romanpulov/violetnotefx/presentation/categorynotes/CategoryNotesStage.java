package com.romanpulov.violetnotefx.Presentation.categorynotes;

import com.romanpulov.violetnotefx.Core.dialogs.AlertDialogs;
import com.romanpulov.violetnotefx.Model.Document;
import com.romanpulov.violetnotefx.Presentation.DialogsHelper;
import com.romanpulov.violetnotefx.Presentation.base.AppStage;
import com.romanpulov.violetnotefx.PropertiesManager;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.Optional;

/**
 * Created by rpulov on 14.03.2016.
 */
public class CategoryNotesStage extends AppStage<CategoryNotesModel, CategoryNotesPresenter> {

    private void saveProperties() {
        PropertiesManager.getInstance().getProperties().setProperty(PropertiesManager.DOCUMENT_FILE_NAME, Document.getInstance().getFileName().getValue());
        PropertiesManager.getInstance().save();
    }

    private void loadProperties() {
        PropertiesManager.getInstance().load();
        String documentFileName = PropertiesManager.getInstance().getProperties().getProperty(PropertiesManager.DOCUMENT_FILE_NAME);
        if (documentFileName != null ) {
            File f = new File(documentFileName);
            if (f.exists())
                controller.loadVNF(f);
        }
    }

    @Override
    protected void afterCreateStage() {
        super.afterCreateStage();

        stage.setOnCloseRequest((e) -> {
            if (model.getInvalidatedData().getValue()) {
                if (!DialogsHelper.queryUnsavedData()) {
                    e.consume();
                } else
                    saveProperties();
            } else {
                saveProperties();
            }
        });

        stage.titleProperty().bind(Bindings.concat("VioletNoteFX - ").
                concat(Document.getInstance().getFileName()).
                concat(new When(model.getInvalidatedData()).then(" * ").otherwise("")));

        switch (model.getLoadFileType()) {
            case FT_IMPORT:
                controller.importPINS(new File(model.getLoadFileName()));
                break;
            case FT_VNF:
                controller.loadVNF(new File(model.getLoadFileName()));
                break;
        }

        loadProperties();




        HBox hb = new HBox();
        hb.setPrefWidth(150);
        hb.setPrefHeight(70);
        hb.setBackground(new Background(new BackgroundFill(Color.rgb(180, 180, 180, 0.5d), CornerRadii.EMPTY, Insets.EMPTY)));
        ProgressIndicator pi = new ProgressIndicator();
        pi.setProgress(-1.);

        Label lb = new Label();
        lb.setStyle("-fx-font-weight: bold");
        lb.setText("Loading");


        hb.setSpacing(10d);
        hb.setPadding(new Insets(10d));
        hb.setAlignment(Pos.CENTER_LEFT);

        hb.getChildren().addAll(pi, lb);

        hb.setStyle("-fx-border-width: 1;" +
                "-fx-border-style: solid inside;");

        DoubleProperty heightProperty = new SimpleDoubleProperty();
        heightProperty.bind(stage.getScene().getWindow().heightProperty().divide(2.));

        /*
        Stage boxStage = new Stage();
        Scene boxScene = new Scene(hb, 200, 200);
        boxStage.setScene(boxScene);
        boxStage.initStyle(StageStyle.UNDECORATED);
        boxScene
        boxStage.show();
        */


        AnchorPane.setTopAnchor(hb, 100d);
        AnchorPane.setLeftAnchor(hb, 100d);

        ((AnchorPane)stage.getScene().getRoot()).getChildren().add(hb);
        //((AnchorPane)stage.getScene().getRoot()).getChildren().remove(hb);

    }
}
