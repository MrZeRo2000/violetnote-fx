package com.romanpulov.violetnotefx.model;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassCategory2;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by rpulov on 01.03.2016.
 */
public class PassCategoryFX {
    private PassCategory2 sourcePassCategory;
    private final SimpleObjectProperty<PassCategoryFX> parentCategory;
    private final SimpleStringProperty categoryName;

    public static PassCategoryFX newEmptyInstance(PassCategoryFX parentCategory) {
        return new PassCategoryFX(parentCategory, null);
    }

    public static PassCategoryFX newInstance(PassCategoryFX parentCategory, PassCategoryFX category) {
        return new PassCategoryFX(parentCategory, category.getCategoryName());
    }

    public PassCategoryFX(PassCategoryFX parentCategory, String categoryName) {
        this.parentCategory = new SimpleObjectProperty<>(parentCategory);
        this.categoryName = new SimpleStringProperty(categoryName);
    }

    public void setSourcePassCategory(PassCategory2 passCategory) {
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

    public PassCategory2 getSourcePassCategory() {
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
