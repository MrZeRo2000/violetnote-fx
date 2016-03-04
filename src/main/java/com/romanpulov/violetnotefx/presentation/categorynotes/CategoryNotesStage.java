package com.romanpulov.violetnotefx.presentation.categorynotes;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by 4540 on 04.03.2016.
 */
public class CategoryNotesStage {

    public static void showStage() {
        CategoryNotesView view = new CategoryNotesView();


        Stage categoryNotesStage = new Stage();
        //categoryNotesStage.setTitle("VioletNoteFX");
        Scene categoryNotesScene = new Scene(view.getView());

        CategoryNotesModel model = (CategoryNotesModel) view.getModelInstance();
        categoryNotesStage.titleProperty().bind(Bindings.concat("VioletNoteFX").concat(new When(model.getInvalidatedData()).then(" * ").otherwise("")));

        categoryNotesStage.setScene(categoryNotesScene);
        categoryNotesStage.show();
    }
}
