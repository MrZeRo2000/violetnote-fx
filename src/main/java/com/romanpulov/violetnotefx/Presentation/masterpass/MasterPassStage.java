package com.romanpulov.violetnotefx.Presentation.masterpass;

import com.romanpulov.violetnotefx.Presentation.base.AppStage;
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
        MasterPassModel masterPassModel = masterPassStage.createStage();
        masterPassStage.stage.setTitle("Master Password for " + f.getName());

        masterPassStage.showModal();

        return masterPassModel.modalResult == ButtonType.OK ? masterPassModel.passwordField_textProperty.getValue() : null;
    }
}
