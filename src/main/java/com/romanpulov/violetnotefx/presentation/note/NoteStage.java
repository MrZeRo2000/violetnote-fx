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

    public static void showStage(PassNoteFX note, ObservableList<PassCategoryFX> passCategoryData) {
        NoteView view = new NoteView();

        Stage stage = new Stage();
        stage.setTitle("Note");
        Scene noteScene = new Scene(view.getView());
        NoteModel model = (NoteModel) view.getModelInstance();
        model.setPassCategoryData(passCategoryData);

        //model.categoryName.setValue(categoryNameData.categoryName);

        stage.setScene(noteScene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        //categoryNameData.modelResult = model.modelResult.getValue();
       //categoryNameData.categoryName = model.categoryName.getValue();
    }

}
