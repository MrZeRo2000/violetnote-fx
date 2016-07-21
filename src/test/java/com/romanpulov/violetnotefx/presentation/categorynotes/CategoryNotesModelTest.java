package com.romanpulov.violetnotefx.Presentation.categorynotes;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotefx.Model.PassCategoryFX;
import com.romanpulov.violetnotefx.Model.PassNoteFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by 4540 on 25.02.2016.
 */
public class CategoryNotesModelTest {

    @Test
    public void testMethod() {
        assertEquals(1, 1);
    }

    public PassData createSamplePassData() {
        PassData data = new PassData();

        // systemTextField_textProperty
        List<PassCategory> passCategoryList = new ArrayList<>();
        PassCategory category1 = new PassCategory("Category 1");
        PassCategory category2 = new PassCategory("Category 2");
        passCategoryList.addAll(Arrays.asList(category1, category2));

        //notes
        List<PassNote> passNoteList = new ArrayList<>();
        passNoteList.add(
          new PassNote(category1, "System 1", "User 1", "Password 1", null, null, null)
        );
        passNoteList.add(
                new PassNote(category1, "System 1", "User 2", "Password 2", null, null, null)
        );

        passNoteList.add(
                new PassNote(category2, "System 2", "User 6", "Password 4", null, null, null)
        );

        data.setPassCategoryList(passCategoryList);
        data.setPassNoteList(passNoteList);
        return data;
    }

    public CategoryNotesModel createSampleCategoryNotesModel() {
        CategoryNotesModel model = new CategoryNotesModel();

        //systemTextField_textProperty
        ObservableList<PassCategoryFX> passCategoryData = FXCollections.observableArrayList();
        PassCategoryFX category1 = new PassCategoryFX(null, "Category 1");
        PassCategoryFX category2 = new PassCategoryFX(null, "Category 2");
        passCategoryData.addAll(Arrays.asList(category1, category2));

        //notes
        ObservableList<PassNoteFX> passNoteData = FXCollections.observableArrayList();
        passNoteData.add(
          new PassNoteFX(category1, "System 1", "User 1", "Password 1", null, null, null)
        );

        passNoteData.add(
                new PassNoteFX(category1, "System 1", "User 2", "Password 2", null, null, null)
        );

        passNoteData.add(
                new PassNoteFX(category2, "System 2", "User 6", "Password 4", null, null, null)
        );

        model.setPassCategoryData(passCategoryData);
        model.setPassNoteData(passNoteData);

        return model;
    }

    @Test
    public void testLoadPassData() {
        CategoryNotesModel model = new CategoryNotesModel();
        PassData passData = createSamplePassData();

        model.readPassData(passData);

        assertEquals(passData.getPassCategoryList().size(), model.getPassCategoryData().size());
        assertEquals(passData.getPassNoteList().size(), model.getPassNoteData().size());

        System.out.println("LoadPassData");
        System.out.println("Categories");
        model.getPassCategoryData().stream().forEach(System.out::println);
        System.out.println("Notes");
        model.getPassNoteData().stream().forEach(System.out::println);
    }

    @Test
    public void testSavePassData() {
        CategoryNotesModel model = createSampleCategoryNotesModel();
        PassData passData = model.writePassData();

        assertEquals(model.getPassCategoryData().size(), passData.getPassCategoryList().size());
        assertEquals(model.getPassNoteData().size(), passData.getPassNoteList().size());

        System.out.println("SavePassData");
        System.out.println("Categories");
        passData.getPassCategoryList().stream().forEach(System.out::println);
        System.out.println("Notes");
        passData.getPassNoteList().stream().forEach(System.out::println);
    }

}
