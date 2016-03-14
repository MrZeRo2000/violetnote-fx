package com.romanpulov.violetnotefx.Presentation.categoryname;

import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
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
        public ButtonType modalResult;
        public String categoryName;

        @Override
        public String toString() {
            return "{ModalResult=" + modalResult + ", CategoryName=" + categoryName + "}";
        }
    }

    public static void showStage(CategoryNameData categoryNameData) {
        CategoryNameView view = new CategoryNameView();

        Stage stage = new Stage();
        stage.setTitle("Category");
        Scene scene = new Scene(view.getView());
        CategoryNameModel model = (CategoryNameModel) view.getModelInstance();

        model.categoryName.setValue(categoryNameData.categoryName);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        categoryNameData.modalResult = model.modalResult;
        categoryNameData.categoryName = model.categoryName.getValue();
    }
}
