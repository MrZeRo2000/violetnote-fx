package com.romanpulov.violetnotefx.presentation.note;

import com.romanpulov.violetnotefx.core.annotation.BoundProperty;
import com.romanpulov.violetnotefx.model.PassCategoryFX;
import com.romanpulov.violetnotefx.model.PassNoteFX;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by rpulov on 01.03.2016.
 */
public class NoteModel {

    public IntegerProperty modelResult = new SimpleIntegerProperty();

    public ObjectProperty<PassNoteFX> passNoteFX = new SimpleObjectProperty<>();
    private ObservableList<PassCategoryFX> passCategoryData = FXCollections.observableArrayList();

    @BoundProperty
    public ObjectProperty<PassCategoryFX> categoryComboBox_valueProperty = new SimpleObjectProperty<>();

    @BoundProperty
    public StringProperty systemTextField_textProperty = new SimpleStringProperty();

    public void setPassCategoryData(ObservableList<PassCategoryFX> passCategoryData) {
        this.passCategoryData.addAll(passCategoryData);
    }

    public ObservableList<PassCategoryFX> getPassCategoryData() {
        return passCategoryData;
    }

    public void setPassNoteFX(PassNoteFX passNoteFX) {
        this.passNoteFX.setValue(passNoteFX);
        categoryComboBox_valueProperty.bindBidirectional(passNoteFX.getCategoryProperty());
        systemTextField_textProperty.bindBidirectional(passNoteFX.getSystemProperty());
    }

    public PassNoteFX getPassNoteFX() {
        return passNoteFX.get();
    }

    public PassCategoryFX getPassCategoryFXFromPathDisplayValue(String pathDisplayValue) {
        for (PassCategoryFX passCategoryFX : passCategoryData) {
            if (passCategoryFX.getPathDisplayValue().equals(pathDisplayValue)) {
                return passCategoryFX;
            }
        }
        return null;
    }

}
