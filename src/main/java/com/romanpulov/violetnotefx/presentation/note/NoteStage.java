package com.romanpulov.violetnotefx.Presentation.note;

import com.romanpulov.violetnotefx.Model.PassCategoryFX;
import com.romanpulov.violetnotefx.Model.PassNoteFX;
import com.romanpulov.violetnotefx.Presentation.base.AppStage;
import javafx.collections.ObservableList;

/**
 * Created by rpulov on 14.03.2016.
 */
public class NoteStage extends AppStage<NoteModel, NotePresenter> {

    private String title;

    private NoteStage(){

    }

    private static NoteStage createEditable(
            ObservableList<PassCategoryFX> passCategoryData,
            PassNoteFX note,
            String title) {
        NoteStage noteStage = new NoteStage();
        noteStage.title = title;
        noteStage.createStage();
        NoteModel noteModel = noteStage.getModel();
        noteModel.setPassCategoryData(passCategoryData);
        noteModel.setPassNoteFX(note);

        return noteStage;
    }

    public static NoteStage createReadOnly(PassNoteFX note) {
        NoteStage noteStage = new NoteStage();
        noteStage.title = "Note View";
        noteStage.createStage();
        NoteModel noteModel = noteStage.getModel();
        noteModel.setPassNoteFXReadOnly(note);

        return noteStage;
    }

    public static NoteStage createForAdd(ObservableList<PassCategoryFX> passCategoryData, PassCategoryFX categoryFX) {
        return createEditable(passCategoryData, PassNoteFX.newEmptyInstance(categoryFX), "Note add");
    }

    public static NoteStage createForEdit(ObservableList<PassCategoryFX> passCategoryData, PassNoteFX note) {
        return createEditable(passCategoryData, PassNoteFX.newInstance(note), "Note Edit");
    }

    public static NoteStage createForDuplicate(ObservableList<PassCategoryFX> passCategoryData, PassNoteFX note) {
        return createEditable(passCategoryData, PassNoteFX.newDuplicateInstance(note), "Note Duplicate");
    }

    @Override
    protected void afterCreateStage() {
        super.afterCreateStage();
        stage.setTitle(title);
    }
}
