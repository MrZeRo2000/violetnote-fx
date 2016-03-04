package com.romanpulov.violetnotefx;

import com.romanpulov.violetnotefx.model.PassCategoryFX;
import com.romanpulov.violetnotefx.presentation.note.NoteModel;
import com.romanpulov.violetnotefx.presentation.note.NoteStage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by 4540 on 04.03.2016.
 */
public class NoteModelTest {

    @Test
    public void testMethod() {
        assertEquals(1, 1);
    }

    public NoteModel createSampleNoteModel() {
        NoteModel model = new NoteModel();

        //systemTextField_textProperty
        ObservableList<PassCategoryFX> passCategoryData = FXCollections.observableArrayList();
        PassCategoryFX category1 = new PassCategoryFX(null, "Category 1");
        PassCategoryFX category2 = new PassCategoryFX(null, "Category 2");
        PassCategoryFX category21 = new PassCategoryFX(category2, "Category 21");
        passCategoryData.addAll(Arrays.asList(category1, category2));

        model.setPassCategoryData(passCategoryData);

        return model;
    }

    @Test
    public void testGetPassCategoryFXFromPathDisplayValue() {
        NoteModel model = createSampleNoteModel();

        PassCategoryFX findCategory =  model.getPassCategoryFXFromPathDisplayValue("Category 2");
        assertNotNull(findCategory);
        assertEquals(findCategory.getCategoryName(), "Category 2");

        findCategory =  model.getPassCategoryFXFromPathDisplayValue("Category 888");
        assertNull(findCategory);
    }


}
