package com.romanpulov.violetnotefx.Model;

import com.romanpulov.violetnotecore.Model.PassCategory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by rpulov on 01.03.2016.
 */
public class PassCategoryFX {
    private PassCategory sourcePassCategory;
    private final SimpleObjectProperty<PassCategoryFX> parentCategory;
    private final SimpleStringProperty categoryName;

    public PassCategoryFX(PassCategoryFX parentCategory, String categoryName) {
        this.parentCategory = new SimpleObjectProperty<>(parentCategory);
        this.categoryName = new SimpleStringProperty(categoryName);
    }

    public void setSourcePassCategory(PassCategory passCategory) {
        sourcePassCategory = passCategory;
    }

    public String getCategoryName() {
        return categoryName.get();
    }

    public void setCategoryName(String value) {
        categoryName.setValue(value);
    }

    public PassCategoryFX getParentCategory() {
        return parentCategory.get();
    }

    public PassCategory getSourcePassCategory() {
        return sourcePassCategory;
    }

    public String getDisplayValue() {
        return categoryName.get();
    }

    public SimpleStringProperty getCategoryNameProperty() {
        return categoryName;
    }

    public String getPathDisplayValue() {
        PassCategoryFX parent = getParentCategory();
        if (parent != null)
            return parent.getPathDisplayValue() + " > " + getCategoryName();
        else
            return getCategoryName();
    }

    @Override
    public String toString() {
        return "{sourcePassCategory =  " + sourcePassCategory.toString() + ", parentCategory = " + parentCategory.get() + ", categoryName = " + categoryName.get() + "}";
    }
}
