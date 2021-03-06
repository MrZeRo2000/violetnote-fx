package com.romanpulov.violetnotefx.presentation.note;

import com.romanpulov.violetnotefx.core.annotation.BoundProperty;
import com.romanpulov.violetnotefx.model.PassCategoryFX;
import com.romanpulov.violetnotefx.model.PassNoteFX;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;

/**
 * Created by rpulov on 01.03.2016.
 */
public class NoteModel {

    public ButtonType modalResult = ButtonType.CANCEL;

    private final ObjectProperty<PassNoteFX> passNoteFX = new SimpleObjectProperty<>();
    private final ObservableList<PassCategoryFX> passCategoryData = FXCollections.observableArrayList();

    @BoundProperty
    public final ObjectProperty<PassCategoryFX> categoryComboBox_valueProperty = new SimpleObjectProperty<>();

    @BoundProperty
    public final StringProperty systemTextField_textProperty = new SimpleStringProperty();

    @BoundProperty
    public final StringProperty passwordTextField_textProperty = new SimpleStringProperty();

    @BoundProperty
    public final StringProperty passwordRetypeTextField_textProperty = new SimpleStringProperty();

    @BoundProperty
    public final StringProperty userTextField_textProperty = new SimpleStringProperty();

    @BoundProperty
    public final StringProperty urlTextField_textProperty = new SimpleStringProperty();

    @BoundProperty
    public final StringProperty infoTextArea_textProperty = new SimpleStringProperty();

    public void setPassCategoryData(ObservableList<PassCategoryFX> passCategoryData) {
        this.passCategoryData.addAll(passCategoryData);
    }

    public ObservableList<PassCategoryFX> getPassCategoryData() {
        return passCategoryData;
    }

    public void setPassNoteFX(PassNoteFX passNoteFX) {
        this.passNoteFX.setValue(passNoteFX);

        categoryComboBox_valueProperty.bindBidirectional(passNoteFX.getCategoryProperty());
        systemTextField_textProperty.bindBidirectional(passNoteFX.getSystemProperty());
        userTextField_textProperty.bindBidirectional(passNoteFX.getUserProperty());
        passwordTextField_textProperty.bindBidirectional(passNoteFX.getRealPasswordProperty());
        urlTextField_textProperty.bindBidirectional(passNoteFX.getUrlProperty());
        infoTextArea_textProperty.bindBidirectional(passNoteFX.getInfoProperty());

        passwordRetypeTextField_textProperty.setValue(passNoteFX.getRealPasswordProperty().getValue());
    }

    public void setPassNoteFXReadOnly(PassNoteFX passNoteFX) {
        //this.passNoteFX.setValue(passNoteFX);
        categoryComboBox_valueProperty.bind(passNoteFX.getCategoryProperty());
        systemTextField_textProperty.bind(passNoteFX.getSystemProperty());
        userTextField_textProperty.bind(passNoteFX.getUserProperty());
        passwordTextField_textProperty.bind(passNoteFX.getRealPasswordProperty());
        passwordRetypeTextField_textProperty.bind(new SimpleStringProperty());
        urlTextField_textProperty.bind(passNoteFX.getUrlProperty());
        infoTextArea_textProperty.bind(passNoteFX.getInfoProperty());
    }

    public PassNoteFX getPassNoteFX() {
        return passNoteFX.get();
    }

    public PassCategoryFX getPassCategoryFXFromPathDisplayValue(String pathDisplayValue) {
        for (PassCategoryFX passCategoryFX : passCategoryData) {
            if (passCategoryFX.getPathDisplayValue().equals(pathDisplayValue)) {
                return passCategoryFX;
            }
        }
        return null;
    }

}
