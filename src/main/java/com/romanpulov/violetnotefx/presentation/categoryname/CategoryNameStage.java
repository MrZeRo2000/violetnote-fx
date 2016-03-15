package com.romanpulov.violetnotefx.Presentation.categoryname;

import com.romanpulov.violetnotefx.Presentation.base.AppStage;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

/**
 * Created by rpulov on 14.03.2016.
 */
public class CategoryNameStage extends AppStage<CategoryNameStage.CategoryNameData, CategoryNameModel, CategoryNamePresenter> {
    public static class CategoryNameData {
        public ButtonType modalResult;
        public String categoryName;

        @Override
        public String toString() {
            return "{ModalResult=" + modalResult + ", CategoryName=" + categoryName + "}";
        }
    }

    @Override
    protected Modality getModality() {
        return Modality.APPLICATION_MODAL;
    }

    @Override
    protected void afterCreateStage() {
        super.afterCreateStage();
        model.categoryName.setValue(data.categoryName);
    }

    @Override
    protected void afterShowScene() {
        data.modalResult = model.modalResult;
        data.categoryName = model.categoryName.getValue();
    }
}
