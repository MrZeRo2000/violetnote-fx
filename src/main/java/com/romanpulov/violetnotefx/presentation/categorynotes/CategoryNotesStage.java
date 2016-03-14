package com.romanpulov.violetnotefx.Presentation.categorynotes;

import com.romanpulov.violetnotefx.Model.Document;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by 4540 on 04.03.2016.
 */
public class CategoryNotesStage {

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

    public static void showStage(CategoryNotesData data) {
        CategoryNotesView view = new CategoryNotesView();

        Stage stage = new Stage();
        Scene categoryNotesScene = new Scene(view.getView());

        CategoryNotesModel model = (CategoryNotesModel) view.getModelInstance();
        CategoryNotesPresenter controller = (CategoryNotesPresenter) view.getControllerInstance();
        stage.titleProperty().bind(Bindings.concat("VioletNoteFX - ").
                concat(Document.getInstance().getFileName()).
                concat(new When(model.getInvalidatedData()).then(" * ").otherwise("")));

        switch (data.getLoadFileType()) {
            case FT_IMPORT:
                controller.loadPINS(new File(data.loadFileName));
                break;
            case FT_VNF:
                controller.loadVNF(new File(data.loadFileName));
                break;
        }

        stage.setScene(categoryNotesScene);
        stage.show();
    }
}
