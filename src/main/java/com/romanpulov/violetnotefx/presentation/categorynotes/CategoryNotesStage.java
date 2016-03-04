package com.romanpulov.violetnotefx.presentation.categorynotes;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by 4540 on 04.03.2016.
 */
public class CategoryNotesStage {

    public static void showStage() {
        CategoryNotesView categoryNotesView = new CategoryNotesView();
        Stage categoryNotesStage = new Stage();
        categoryNotesStage.setTitle("VioletNoteFX");

        Scene categoryNotesScene = new Scene(categoryNotesView.getView());
        categoryNotesStage.setScene(categoryNotesScene);
        categoryNotesStage.show();
    }
}
