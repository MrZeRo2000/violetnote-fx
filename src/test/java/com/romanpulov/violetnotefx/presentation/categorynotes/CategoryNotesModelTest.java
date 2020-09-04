package com.romanpulov.violetnotefx.presentation.categorynotes;

import com.romanpulov.violetnotecore.Model.*;
import com.romanpulov.violetnotefx.model.PassCategoryFX;
import com.romanpulov.violetnotefx.model.PassNoteFX;
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

    public PassData2 createSamplePassData() {
        PassData2 data = new PassData2();

        // systemTextField_textProperty
        PassCategory2 category1 = new PassCategory2("Category 1");
        category1.setNoteList(new ArrayList<>());
        PassCategory2 category2 = new PassCategory2("Category 2");
        category2.setNoteList(new ArrayList<>());
        List<PassCategory2> passCategoryList = new ArrayList<>(Arrays.asList(category1, category2));

        //notes
        category1.getNoteList().add(
          new PassNote2("System 1", "User 1", "Password 1", null, null, null, null)
        );

        category1.getNoteList().add(
                new PassNote2("System 1", "User 2", "Password 2", null, null, null, null)
        );

        category2.getNoteList().add(
                new PassNote2( "System 2", "User 6", "Password 4", null, null, null, null)
        );

        data.setCategoryList(passCategoryList);
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
          new PassNoteFX(category1, "System 1", "User 1", "Password 1", null, null)
        );

        passNoteData.add(
                new PassNoteFX(category1, "System 1", "User 2", "Password 2", null, null)
        );

        passNoteData.add(
                new PassNoteFX(category2, "System 2", "User 6", "Password 4", null, null)
        );

        model.setPassCategoryData(passCategoryData);
        model.setPassNoteData(passNoteData);

        return model;
    }

    @Test
    public void testLoadPassData() {
        CategoryNotesModel model = new CategoryNotesModel();
        PassData2 passData2 = createSamplePassData();

        model.readPassData(passData2);

        assertEquals(passData2.getCategoryList().size(), model.getPassCategoryData().size());

        int passNoteListSize = passData2.getCategoryList().stream().mapToInt(passCategory2 -> passCategory2.getNoteList().size()).sum();

        assertEquals(passNoteListSize, model.getPassNoteData().size());

        System.out.println("LoadPassData");
        System.out.println("Categories");
        model.getPassCategoryData().forEach(System.out::println);
        System.out.println("Notes");
        model.getPassNoteData().forEach(System.out::println);
    }

    @Test
    public void testSavePassData() {
        CategoryNotesModel model = createSampleCategoryNotesModel();
        PassData2 passData2 = model.writePassData();

        assertEquals(model.getPassCategoryData().size(), passData2.getCategoryList().size());
        assertEquals(model.getPassNoteData().size(),
                passData2.getCategoryList().stream().mapToInt(passCategory2 -> passCategory2.getNoteList().size()).sum());

        System.out.println("SavePassData");
        System.out.println("Categories");
        passData2.getCategoryList().forEach(System.out::println);
        System.out.println("Notes");
        passData2.getCategoryList().forEach(passCategory2 -> passCategory2.getNoteList().forEach(System.out::println));
    }

}
