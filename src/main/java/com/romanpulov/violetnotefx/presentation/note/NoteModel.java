package com.romanpulov.violetnotefx.presentation.note;

import com.romanpulov.violetnotefx.model.PassCategoryFX;
import com.romanpulov.violetnotefx.model.PassNoteFX;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by rpulov on 01.03.2016.
 */
public class NoteModel {

    public IntegerProperty modelResult = new SimpleIntegerProperty();

    public ObjectProperty<PassNoteFX> passNoteFX;

    private ObservableList<PassCategoryFX> passCategoryData = FXCollections.observableArrayList();;

    public void setPassCategoryData(ObservableList<PassCategoryFX> passCategoryData) {
        this.passCategoryData.addAll(passCategoryData);
    }

    public ObservableList<PassCategoryFX> getPassCategoryData() {
        return passCategoryData;
    }

    public void setPassNoteFX(PassNoteFX passNoteFX) {
        this.passNoteFX = new SimpleObjectProperty<>(passNoteFX);
    }

    public PassNoteFX getPassNoteFX() {
        return passNoteFX.get();
    }

}
