package com.romanpulov.violetnotefx.presentation.masterpass;

import com.romanpulov.violetnotefx.core.annotation.BoundProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ButtonType;

/**
 * Created by rpulov on 23.01.2016.
 */
public class MasterPassModel {

    @BoundProperty
    public final StringProperty passwordField_textProperty = new SimpleStringProperty();

    public ButtonType modalResult = ButtonType.CANCEL;
}
