package com.romanpulov.violetnotefx.Presentation.note;

import com.romanpulov.violetnotefx.MainApp;
import com.romanpulov.violetnotefx.Model.PassCategoryFX;
import com.romanpulov.violetnotefx.Model.PassNoteFX;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by rpulov on 01.03.2016.
 */
public class NoteStage {

    public static class NoteData {
        public ButtonType modalResult;
        public PassNoteFX passNoteFX;
        public ObservableList<PassCategoryFX> passCategoryData;
    }

    public static void showStage(NoteData noteData) {
        NoteView view = new NoteView();

        Stage stage = new Stage();
        stage.setTitle("Note");
        Scene noteScene = new Scene(view.getView());
        NoteModel model = (NoteModel) view.getModelInstance();

        model.setPassCategoryData(noteData.passCategoryData);
        model.setPassNoteFX(noteData.passNoteFX);

        stage.setScene(noteScene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        MainApp.setupStageIcons(stage);
        stage.showAndWait();

        noteData.modalResult = model.modalResult;
        noteData.passNoteFX = model.passNoteFX.getValue();
    }
}
