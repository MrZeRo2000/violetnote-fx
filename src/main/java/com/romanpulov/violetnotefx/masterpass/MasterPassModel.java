package com.romanpulov.violetnotefx.masterpass;

import com.romanpulov.violetnotefx.annotation.BoundProperty;
import com.romanpulov.violetnotefx.annotation.ModelOperation;
import com.romanpulov.violetnotefx.annotation.ModelOperationType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.annotation.PostConstruct;

/**
 * Created by rpulov on 23.01.2016.
 */
public class MasterPassModel {

    @BoundProperty
    StringProperty passwordField_textProperty = new SimpleStringProperty();

    @ModelOperation(operationType = ModelOperationType.LOAD)
    private void modelLoad() {
        System.out.println("In Model OperationType = load");
        passwordField_textProperty.setValue("initialvalue");
    }

    @ModelOperation(operationType = ModelOperationType.UNLOAD)
    private void modelUnload() {
        System.out.println("In Model OperationType = unload");
        System.out.println("PasswordField=" + passwordField_textProperty.getValue());
    }
}
