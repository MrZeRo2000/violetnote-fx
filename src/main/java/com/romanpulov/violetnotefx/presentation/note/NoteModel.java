package com.romanpulov.violetnotefx.presentation.note;

import com.romanpulov.violetnotefx.model.PassCategoryFX;
import javafx.collections.ObservableList;

/**
 * Created by rpulov on 01.03.2016.
 */
public class NoteModel {

    private ObservableList<PassCategoryFX> passCategoryData;

    public void setPassCategoryData(ObservableList<PassCategoryFX> passCategoryData) {
        this.passCategoryData = passCategoryData;
    }

}
