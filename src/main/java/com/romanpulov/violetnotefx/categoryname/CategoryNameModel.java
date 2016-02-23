package com.romanpulov.violetnotefx.categoryname;

import com.romanpulov.violetnotefx.core.annotation.BoundProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by rpulov on 22.02.2016.
 */
public class CategoryNameModel {

    public IntegerProperty modelResult = new SimpleIntegerProperty();

    public StringProperty categoryName = new SimpleStringProperty();
}
