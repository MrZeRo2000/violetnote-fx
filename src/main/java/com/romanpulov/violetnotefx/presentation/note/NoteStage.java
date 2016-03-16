package com.romanpulov.violetnotefx.Presentation.note;

import com.romanpulov.violetnotefx.Presentation.base.AppStage;

/**
 * Created by rpulov on 14.03.2016.
 */
public class NoteStage extends AppStage<NoteModel, NotePresenter> {
    @Override
    protected void afterCreateStage() {
        super.afterCreateStage();
        stage.setTitle("Note Edit");
    }
}
