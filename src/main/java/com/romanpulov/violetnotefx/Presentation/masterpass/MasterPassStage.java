package com.romanpulov.violetnotefx.Presentation.masterpass;

import com.romanpulov.violetnotefx.Presentation.base.AppStage;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

/**
 * Created by rpulov on 14.03.2016.
 */
public class MasterPassStage extends AppStage {
    public static class MasterPassData {
        public ButtonType modalResult;
        public String masterPass;

        public MasterPassData(String masterPass) {
            this.masterPass = masterPass;
        }

        @Override
        public String toString() {
            return "{ModalResult=" + modalResult + ", MasterPass=" + masterPass + "}";
        }
    }

    private MasterPassData masterPassData;
    private MasterPassModel masterPassModel;

    public MasterPassStage(Object data) {
        super(data);
        masterPassData = (MasterPassData) data;
    }

    @Override
    protected Modality getModality() {
        return Modality.APPLICATION_MODAL;
    }

    @Override
    protected void afterCreateStage() {
        super.afterCreateStage();
        stage.setTitle("Master Password");
        masterPassModel = (MasterPassModel) model;
    }

    @Override
    protected void afterShowScene() {
        masterPassData.modalResult = masterPassModel.modalResult;
        masterPassData.masterPass = masterPassModel.passwordField_textProperty.getValue();
    }

    public static String queryMasterPass(String pass) {
        MasterPassData data = new MasterPassData(pass);
        (new MasterPassStage(data)).show();
        return data.modalResult == ButtonType.OK ? data.masterPass : null;
    }

}
