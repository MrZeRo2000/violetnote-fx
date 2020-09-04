package com.romanpulov.violetnotefx;

import com.romanpulov.violetnotefx.presentation.categorynotes.CategoryNotesModel;

import java.io.File;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ModelLoadFileTest {
    private final String FILE_NAME_V1 = "data/test1.vnf";
    private final String FILE_NAME_V2 = "data/test1_v2.vnf";
    private final String PASSWORD = "123456";

    private static void validateModel(CategoryNotesModel model) {
        assertEquals(4, model.getPassCategoryData().size());
        assertEquals(7, model.getPassNoteData().size());
    }

    @Test
    public void loadFileV1() {
        CategoryNotesModel categoryNotesModel = new CategoryNotesModel();
        boolean loadResult = categoryNotesModel.loadFile(new File(FILE_NAME_V1), PASSWORD);

        assertTrue(loadResult);
        validateModel(categoryNotesModel);

        loadResult = categoryNotesModel.loadFile(new File(FILE_NAME_V1), PASSWORD + "123");
        assertFalse(loadResult);
    }

    @Test
    public void loadFileV2() {
        CategoryNotesModel categoryNotesModel = new CategoryNotesModel();
        boolean loadResult = categoryNotesModel.loadFile(new File(FILE_NAME_V2), PASSWORD);

        assertTrue(loadResult);
        validateModel(categoryNotesModel);

        loadResult = categoryNotesModel.loadFile(new File(FILE_NAME_V2), PASSWORD + "123");
        assertFalse(loadResult);
    }
}
