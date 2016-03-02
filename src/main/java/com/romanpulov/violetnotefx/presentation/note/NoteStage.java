package com.romanpulov.violetnotefx.presentation.note;

import com.romanpulov.violetnotefx.model.PassCategoryFX;
import com.romanpulov.violetnotefx.model.PassNoteFX;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by rpulov on 01.03.2016.
 */
public class NoteStage {

    public static class NoteData {
        public int modelResult;
        public PassNoteFX passNoteFX;
    }

    public static void showStage(NoteData noteData) {
        NoteView view = new NoteView();

        Stage stage = new Stage();
        stage.setTitle("Note");
        Scene noteScene = new Scene(view.getView());
        NoteModel model = (NoteModel) view.getModelInstance();

        stage.setScene(noteScene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        noteData.modelResult = model.modelResult.getValue();
        noteData.passNoteFX = model.passNoteFX.getValue();
    }
}
