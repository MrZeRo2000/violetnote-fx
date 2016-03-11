package com.romanpulov.violetnotefx.Presentation.masterpass;

import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by 4540 on 11.03.2016.
 */
public class MasterPassStage {
    public static class MasterPassData {
        public ButtonType modalResult;
        public String masterPass;

        public MasterPassData(String masterPass) {
            this.masterPass = masterPass;
        }

        public MasterPassData() {

        }

        @Override
        public String toString() {
            return "{ModalResult=" + modalResult + ", MasterPass=" + masterPass + "}";
        }
    }

    public static void showStage(MasterPassData data) {
        MasterPassView view = new MasterPassView();

        Stage stage = new Stage();
        stage.setTitle("Master Password");
        Scene scene = new Scene(view.getView());
        MasterPassModel model = (MasterPassModel) view.getModelInstance();

        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        data.modalResult = model.modalResult;
        data.masterPass = model.passwordField_textProperty.getValue();
    }

    public static String queryMasterPass(String pass) {
        MasterPassData data = new MasterPassData(pass);
        showStage(data);
        return data.modalResult == ButtonType.OK ? data.masterPass : null;
    }
}
