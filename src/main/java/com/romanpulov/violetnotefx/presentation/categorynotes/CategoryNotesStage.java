package com.romanpulov.violetnotefx.Presentation.categorynotes;

import com.romanpulov.violetnotefx.Model.Document;
import com.romanpulov.violetnotefx.Presentation.base.AppStage;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.stage.Modality;

import java.io.File;

/**
 * Created by rpulov on 14.03.2016.
 */
public class CategoryNotesStage extends AppStage {
    public static class CategoryNotesData {
        private String loadFileName;
        private Document.FileType loadFileType = Document.FileType.FT_NONE;

        public void updateFileType() {
            int dotPos = loadFileName.lastIndexOf(".");
            if (dotPos > 0) {
                String extension = loadFileName.substring(dotPos + 1).toLowerCase();
                loadFileType = Document.FILE_TYPES.get(extension);
            } else
                loadFileType = Document.FileType.FT_NONE;
        }

        public void setLoadFileName(String loadFileName) {
            this.loadFileName = loadFileName;
            updateFileType();
        }

        public Document.FileType getLoadFileType() {
            return loadFileType;
        }
    }

    private CategoryNotesData categoryNotesData;

    public CategoryNotesStage(Object data) {
        super(data);
        categoryNotesData = (CategoryNotesData)data;
    }

    @Override
    protected Modality getModality() {
        return Modality.NONE;
    }

    @Override
    protected void afterCreateStage() {
        super.afterCreateStage();

        CategoryNotesModel model = (CategoryNotesModel) view.getModelInstance();
        CategoryNotesPresenter controller = (CategoryNotesPresenter) view.getControllerInstance();

        stage.titleProperty().bind(Bindings.concat("VioletNoteFX - ").
                concat(Document.getInstance().getFileName()).
                concat(new When(model.getInvalidatedData()).then(" * ").otherwise("")));

        switch (categoryNotesData.getLoadFileType()) {
            case FT_IMPORT:
                controller.loadPINS(new File(categoryNotesData.loadFileName));
                break;
            case FT_VNF:
                controller.loadVNF(new File(categoryNotesData.loadFileName));
                break;
        }
    }
}
