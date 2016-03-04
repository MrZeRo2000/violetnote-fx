package com.romanpulov.violetnotefx.presentation.categoryname;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ButtonType;

/**
 * Created by rpulov on 22.02.2016.
 */
public class CategoryNameModel {

    public ButtonType modalResult = ButtonType.CANCEL;

    public StringProperty categoryName = new SimpleStringProperty();
}
