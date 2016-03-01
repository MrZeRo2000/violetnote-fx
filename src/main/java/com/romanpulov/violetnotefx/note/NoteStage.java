package com.romanpulov.violetnotefx.note;

import com.romanpulov.violetnotefx.categorynotes.CategoryNotesModel;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by rpulov on 01.03.2016.
 */
public class NoteStage {

    public static void showStage(CategoryNotesModel.PassNoteFX note) {
        NoteView view = new NoteView();

        Stage stage = new Stage();
        stage.setTitle("Note");
        Scene noteScene = new Scene(view.getView());
        NoteModel model = (NoteModel) view.getModelInstance();

        //model.categoryName.setValue(categoryNameData.categoryName);

        stage.setScene(noteScene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        //categoryNameData.modelResult = model.modelResult.getValue();
       //categoryNameData.categoryName = model.categoryName.getValue();
    }

}
