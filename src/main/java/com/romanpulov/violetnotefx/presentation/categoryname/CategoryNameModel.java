package com.romanpulov.violetnotefx.Presentation.categoryname;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ButtonType;

/**
 * Created by rpulov on 22.02.2016.
 */
public class CategoryNameModel {

    public ButtonType modalResult = ButtonType.CANCEL;

    public final StringProperty categoryName = new SimpleStringProperty();
}
