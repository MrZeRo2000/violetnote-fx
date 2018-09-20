package com.romanpulov.violetnotefx.presentation.masterpass;

import com.romanpulov.violetnotefx.presentation.base.AppStage;
import javafx.scene.control.ButtonType;

import java.io.File;

/**
 * Created by rpulov on 14.03.2016.
 */
public class MasterPassStage extends AppStage<MasterPassModel, MasterPassPresenter> {

    private MasterPassStage() {

    }

    public static String queryMasterPass(File f, String pass) {
        MasterPassStage masterPassStage = new MasterPassStage();
        masterPassStage.createStage();
        MasterPassModel masterPassModel = masterPassStage.getModel();
        masterPassStage.stage.setTitle("Master Password for " + f.getName());

        masterPassStage.showModal();

        return masterPassModel.modalResult == ButtonType.OK ? masterPassModel.passwordField_textProperty.getValue() : null;
    }
}
