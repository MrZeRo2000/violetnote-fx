package com.romanpulov.violetnotefx.presentation.note;

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

    public ObjectProperty<PassCategoryFX> category = new SimpleObjectProperty<>();
    public StringProperty system = new SimpleStringProperty();

    public void setPassCategoryData(ObservableList<PassCategoryFX> passCategoryData) {
        this.passCategoryData.addAll(passCategoryData);
    }

    public ObservableList<PassCategoryFX> getPassCategoryData() {
        return passCategoryData;
    }

    public void setPassNoteFX(PassNoteFX passNoteFX) {
        this.passNoteFX.setValue(passNoteFX);
        category.bindBidirectional(passNoteFX.getCategoryProperty());
        system.bindBidirectional(passNoteFX.getSystemProperty());
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
