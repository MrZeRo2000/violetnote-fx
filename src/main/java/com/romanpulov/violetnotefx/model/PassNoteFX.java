package com.romanpulov.violetnotefx.Model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by rpulov on 01.03.2016.
 */
public class PassNoteFX {
    private final SimpleObjectProperty<PassCategoryFX> category;
    //private final SimpleStringProperty categoryName;
    private final SimpleStringProperty system;
    private final SimpleStringProperty user;
    private final SimpleStringProperty password;
    private final SimpleStringProperty realPassword;
    private final SimpleStringProperty comments;
    private final SimpleStringProperty custom;
    private final SimpleStringProperty info;

    public static PassNoteFX newInstance(PassNoteFX passNoteFX) {
        return new PassNoteFX(
                passNoteFX.getCategory(), passNoteFX.getSystem(), passNoteFX.getUser(), passNoteFX.getRealPassword(),
                passNoteFX.getComments(), passNoteFX.getCustom(), passNoteFX.getInfo());
    }

    public PassNoteFX(PassCategoryFX category) {
        this(category, null, null, null, null, null, null);
    }

    public PassNoteFX(PassCategoryFX category, String system, String user, String password, String comments, String custom, String info) {
        this.category = new SimpleObjectProperty<PassCategoryFX>(category);
        this.system = new SimpleStringProperty(system);
        this.user = new SimpleStringProperty(user);
        this.password = new SimpleStringProperty(password);
        this.realPassword = new SimpleStringProperty(password);
        this.password.bind(this.realPassword);
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

    public ObjectProperty<PassCategoryFX> getCategoryProperty() {
        return category;
    }

    public String getSystem() {
        return system.get();
    }

    public StringProperty getSystemProperty() {
        return system;
    }

    public String getUser() {
        return user.get();
    }

    public StringProperty getUserProperty() {
        return user;
    }

    public String getPassword() {
        return password.get() == null ? null : new String(new char[password.get().length()]).replace("\0", "*");
    }

    public StringProperty getPasswordProperty() {
        return password;
    }

    public String getRealPassword() {
        return realPassword.get();
    }

    public StringProperty getRealPasswordProperty() {
        return realPassword;
    }

    public String getComments() {
        return comments.get();
    }

    public StringProperty getCommentsProperty() {
        return comments;
    }

    public String getCustom() {
        return custom.get();
    }

    public StringProperty getCustomProperty() {
        return custom;
    }

    public String getInfo() {
        return info.get();
    }

    public StringProperty getInfoProperty() {
        return info;
    }

    @Override
    public String toString() {
        return "{systemTextField_textProperty = " + getCategory() + ", systemTextField_textProperty = " + getSystem() + ", user = " + getUser() + ", password = " + getPassword() +
                ", comments = " + getComments() + ", custom = " + getCustom() + ", info = " + getInfo() + "}";
    }
}
