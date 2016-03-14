package com.romanpulov.violetnotefx.Presentation.categoryname;

import com.romanpulov.violetnotefx.Presentation.base.AppStage;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

/**
 * Created by rpulov on 14.03.2016.
 */
public class CategoryNameStage extends AppStage {
    public static class CategoryNameData {
        public ButtonType modalResult;
        public String categoryName;

        @Override
        public String toString() {
            return "{ModalResult=" + modalResult + ", CategoryName=" + categoryName + "}";
        }
    }

    private CategoryNameData categoryNameData;
    private CategoryNameModel categoryNameModel;

    public CategoryNameStage(Object data) {
        super(data);
        categoryNameData = (CategoryNameData)data;
    }

    @Override
    protected Modality getModality() {
        return Modality.APPLICATION_MODAL;
    }

    @Override
    protected void afterCreateStage() {
        super.afterCreateStage();
        categoryNameModel = (CategoryNameModel)model;
        categoryNameModel.categoryName.setValue(categoryNameData.categoryName);
    }

    @Override
    protected void afterShowScene() {
        categoryNameData.modalResult = categoryNameModel.modalResult;
        categoryNameData.categoryName = categoryNameModel.categoryName.getValue();
    }
}
