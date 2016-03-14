package com.romanpulov.violetnotefx.Presentation.categorynotes;

import com.romanpulov.violetnotefx.Model.Document;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 4540 on 04.03.2016.
 */
public class CategoryNotesStage {
    public enum FileType {FT_IMPORT, FT_VNF};

    private static final Map<String, FileType> FILE_TYPES = new HashMap<>();
    static
    {
        FILE_TYPES.put("csv", FileType.FT_IMPORT);
        FILE_TYPES.put("vnf", FileType.FT_VNF);
    }

    public static class CategoryNotesData {
        private String loadFileName;
        private FileType ft;

        public void updateFileType() {
            int dotPos = loadFileName.lastIndexOf(".");
            if (dotPos > 0) {
                String extension = loadFileName.substring(dotPos + 1).toLowerCase();
                ft = FILE_TYPES.get(extension);
            }
        }

        public void setLoadFileName(String loadFileName) {
            this.loadFileName = loadFileName;
            updateFileType();
        }

        public FileType getFileType() {
            return ft;
        }
    }

    public static void showStage(CategoryNotesData data) {
        CategoryNotesView view = new CategoryNotesView();

        Stage stage = new Stage();
        Scene categoryNotesScene = new Scene(view.getView());

        CategoryNotesModel model = (CategoryNotesModel) view.getModelInstance();
        stage.titleProperty().bind(Bindings.concat("VioletNoteFX - ").
                concat(Document.getInstance().getFileName()).
                concat(new When(model.getInvalidatedData()).then(" * ").otherwise("")));

        switch (data.getFileType()) {
            case FT_IMPORT:
                model.importPINSFile(new File(data.loadFileName));
                break;
        }

        stage.setScene(categoryNotesScene);
        stage.show();
    }
}
