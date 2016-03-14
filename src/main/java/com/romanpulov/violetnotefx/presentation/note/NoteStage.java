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
public class NoteStage extends AppStage {
    public static class NoteData {
        public ButtonType modalResult;
        public PassNoteFX passNoteFX;
        public ObservableList<PassCategoryFX> passCategoryData;
    }

    private NoteModel noteModel;
    private NoteData noteData;

    public NoteStage(Object data) {
        super(data);
        noteData = (NoteData) data;
    }

    @Override
    protected Modality getModality() {
        return Modality.APPLICATION_MODAL;
    }

    @Override
    protected void afterCreateStage() {
        super.afterCreateStage();
        noteModel = (NoteModel) view.getModelInstance();

        noteModel.setPassCategoryData(noteData.passCategoryData);
        noteModel.setPassNoteFX(noteData.passNoteFX);
    }

    @Override
    protected void afterShowScene() {
        super.afterShowScene();
        noteData.modalResult = noteModel.modalResult;
        noteData.passNoteFX = noteModel.passNoteFX.getValue();
    }
}
