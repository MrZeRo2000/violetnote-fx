package com.romanpulov.violetnotefx.presentation.categoryname;

import com.romanpulov.violetnotefx.core.annotation.BoundProperty;
import com.romanpulov.violetnotefx.model.PassCategoryFX;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ButtonType;

/**
 * Created by rpulov on 22.02.2016.
 */
public class CategoryNameModel {

    public ButtonType modalResult = ButtonType.CANCEL;

    private final ObjectProperty<PassCategoryFX> passCategoryFX = new SimpleObjectProperty<>();

    @BoundProperty
    private final StringProperty categoryNameField_textProperty = new SimpleStringProperty();

    public void setPassCategoryFX(PassCategoryFX passCategoryFX) {
        this.passCategoryFX.setValue(passCategoryFX);
        categoryNameField_textProperty.bindBidirectional(passCategoryFX.getCategoryNameProperty());
    }

    public void setPassCategoryFXReadOnly(PassCategoryFX passCategoryFX) {
        this.passCategoryFX.setValue(passCategoryFX);
        categoryNameField_textProperty.bind(passCategoryFX.getCategoryNameProperty());
    }

    public PassCategoryFX getPassCategoryFX() {
        return passCategoryFX.get();
    }
}
