package com.romanpulov.violetnotefx.Presentation.categorynotes;

import com.romanpulov.violetnotefx.Model.Document;
import com.romanpulov.violetnotefx.Presentation.base.AppStage;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;

import java.io.File;

/**
 * Created by rpulov on 14.03.2016.
 */
public class CategoryNotesStage extends AppStage<CategoryNotesModel, CategoryNotesPresenter> {
    @Override
    protected void afterCreateStage() {
        super.afterCreateStage();

        stage.titleProperty().bind(Bindings.concat("VioletNoteFX - ").
                concat(Document.getInstance().getFileName()).
                concat(new When(model.getInvalidatedData()).then(" * ").otherwise("")));

        switch (model.getLoadFileType()) {
            case FT_IMPORT:
                controller.loadPINS(new File(model.getLoadFileName()));
                break;
            case FT_VNF:
                controller.loadVNF(new File(model.getLoadFileName()));
                break;
        }
    }
}
