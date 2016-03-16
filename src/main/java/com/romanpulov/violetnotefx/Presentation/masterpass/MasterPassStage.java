package com.romanpulov.violetnotefx.Presentation.masterpass;

import com.romanpulov.violetnotefx.Presentation.base.AppStage;
import javafx.scene.control.ButtonType;

/**
 * Created by rpulov on 14.03.2016.
 */
public class MasterPassStage extends AppStage<MasterPassModel, MasterPassPresenter> {
    @Override
    protected void afterCreateStage() {
        super.afterCreateStage();
        stage.setTitle("Master Password");
    }

    public static String queryMasterPass(String pass) {
        MasterPassStage masterPassStage = new MasterPassStage();
        MasterPassModel masterPassModel = masterPassStage.createStage();
        masterPassStage.showModal();
        return masterPassModel.modalResult == ButtonType.OK ? masterPassModel.passwordField_textProperty.getValue() : null;
    }
}
