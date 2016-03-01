package com.romanpulov.violetnotefx.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by rpulov on 01.03.2016.
 */
public class PassNoteFX {
    private final SimpleObjectProperty<PassCategoryFX> category;
    //private final SimpleStringProperty categoryName;
    private final SimpleStringProperty system;
    private final SimpleStringProperty user;
    private final SimpleStringProperty password;
    private final SimpleStringProperty comments;
    private final SimpleStringProperty custom;
    private final SimpleStringProperty info;

    public PassNoteFX(PassCategoryFX category) {
        this(category, null, null, null, null, null, null);
    }

    public PassNoteFX(PassCategoryFX category, String system, String user, String password, String comments, String custom, String info) {
        this.category = new SimpleObjectProperty<PassCategoryFX>(category);
        this.system = new SimpleStringProperty(system);
        this.user = new SimpleStringProperty(user);
        this.password = new SimpleStringProperty(password);
        this.comments = new SimpleStringProperty(comments);
        this.custom = new SimpleStringProperty(custom);
        this.info = new SimpleStringProperty(info);
    }

    public String getCategoryName() {
        return (category.get() == null) ? null : category.get().getCategoryName();
    }

    public PassCategoryFX getCategory() {
        return category.get();
    }

    public String getSystem() {
        return system.get();
    }

    public String getUser() {
        return user.get();
    }

    public String getPassword() {
        return new String(new char[password.get().length()]).replace("\0", "*");
    }

    public String getComments() {
        return comments.get();
    }

    public String getCustom() {
        return custom.get();
    }

    public String getInfo() {
        return info.get();
    }

    @Override
    public String toString() {
        return "{category = " + getCategory() + ", system = " + getSystem() + ", user = " + getUser() + ", password = " + getPassword() +
                ", comments = " + getComments() + ", custom = " + getCustom() + ", info = " + getInfo() + "}";
    }

}
