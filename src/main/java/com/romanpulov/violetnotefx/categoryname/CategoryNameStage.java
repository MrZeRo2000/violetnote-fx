package com.romanpulov.violetnotefx.categoryname;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 4540 on 23.02.2016.
 */
public class CategoryNameStage {
    private static final Logger log = LoggerFactory.getLogger(CategoryNameStage.class);

    public static class CategoryNameData {
        public int modelResult;
        public String categoryName;
    }

    public static void showStage(CategoryNameData categoryNameData) {
        CategoryNameView view = new CategoryNameView();

        Stage stage = new Stage();
        stage.setTitle("Category");
        Scene masterPassScene = new Scene(view.getView());
        CategoryNameModel model = (CategoryNameModel) view.getModelInstance();

        model.categoryName.setValue(categoryNameData.categoryName);

        stage.setScene(masterPassScene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        categoryNameData.modelResult = model.modelResult.getValue();
        categoryNameData.categoryName = model.categoryName.getValue();
    }
}
