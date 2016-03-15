package com.romanpulov.violetnotefx.Presentation.note;

import com.romanpulov.violetnotefx.Model.PassCategoryFX;
import com.romanpulov.violetnotefx.Model.PassNoteFX;
import com.romanpulov.violetnotefx.Presentation.base.AppStage;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

/**
 * Created by rpulov on 14.03.2016.
 */
public class NoteStage extends AppStage<NoteStage.NoteData, NoteModel, NotePresenter> {
    public static class NoteData {
        public ButtonType modalResult;
        public PassNoteFX passNoteFX;
        public ObservableList<PassCategoryFX> passCategoryData;
    }

    @Override
    protected Modality getModality() {
        return Modality.APPLICATION_MODAL;
    }

    @Override
    protected void afterCreateStage() {
        super.afterCreateStage();

        model.setPassCategoryData(data.passCategoryData);
        model.setPassNoteFX(data.passNoteFX);
    }

    @Override
    protected void afterShowScene() {
        super.afterShowScene();
        data.modalResult = model.modalResult;
        data.passNoteFX = model.passNoteFX.getValue();
    }
}
