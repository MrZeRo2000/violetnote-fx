package com.romanpulov.violetnotefx.presentation.categorynotes;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotefx.model.Document;
import com.romanpulov.violetnotefx.model.PassCategoryFX;
import com.romanpulov.violetnotefx.model.PassNoteFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 4540 on 22.02.2016.
 */
public class CategoryNotesModel {

    private ObservableList<PassNoteFX> passNoteData;
    private ObservableList<PassCategoryFX> passCategoryData;

    public ObservableList<PassCategoryFX> getPassCategoryData() {
        return passCategoryData;
    }

    public void setPassCategoryData(ObservableList<PassCategoryFX> passCategoryData) {
        this.passCategoryData = passCategoryData;
    }

    public ObservableList<PassNoteFX> getPassNoteData() {
        return passNoteData;
    }

    public void setPassNoteData(ObservableList<PassNoteFX> passNoteData) {
        this.passNoteData = passNoteData;
    }

    private PassCategoryFX addPassCategoryFX(PassCategory passCategory) {
        PassCategory parentPassCategory = passCategory.getParentCategory();
        if (parentPassCategory == null) {
            PassCategoryFX newPassCategoryFX = new PassCategoryFX(null, passCategory.getCategoryName());
            newPassCategoryFX.setSourcePassCategory(passCategory);
            return newPassCategoryFX;
        } else {
            return addPassCategoryFX(passCategory);
        }
    }

    private PassCategoryFX findSourcePassCategory(PassCategory passCategory) {
        for (PassCategoryFX p : passCategoryData) {
            if (p.getSourcePassCategory().equals(passCategory))
                return p;
        }
        return null;
    }

    public PassCategoryFX findChildPassCategoryName(PassCategoryFX parentPassCategory, String categoryName) {
        for (PassCategoryFX p : passCategoryData) {
            if ((p.getParentCategory() == parentPassCategory) && (p.getCategoryName().equals(categoryName))) {
                return p;
            }
        }
        return null;
    }

    public boolean hasCategoryNotes(PassCategoryFX passCategoryFX) {
        if (passCategoryFX == null)
            return false;
        else {
            for (PassNoteFX n : passNoteData) {
                if (n.getCategory().equals(passCategoryFX))
                    return true;
            }
            return false;
        }
    }

    public boolean isLeafCategory(PassCategoryFX passCategoryFX) {
        for (PassCategoryFX p : passCategoryData) {
            if (p.getParentCategory().equals(passCategoryFX))
                return false;
        }
        return true;
    }

    public void loadCategoryData(List<PassCategory> passCategoryList) {
        passCategoryData = FXCollections.observableArrayList();
        passCategoryList.stream().forEach((passCategory -> {
            // find existing
            PassCategoryFX passCategoryFX = findSourcePassCategory(passCategory);
            // if not found then add
            if (passCategoryFX == null) {
                PassCategoryFX newPassCategoryFX = addPassCategoryFX(passCategory);
                if (newPassCategoryFX != null){
                    passCategoryData.add(newPassCategoryFX);
                }
            }
        }));
    }

    public void loadNoteData(List<PassNote> passNoteList) {
        passNoteData =  FXCollections.observableArrayList();
        passNoteList.stream().forEach((passNote)-> {
            PassCategoryFX passCategoryFX = findSourcePassCategory(passNote.getPassCategory());
            if (passCategoryFX != null)
                passNoteData.add(new PassNoteFX(passCategoryFX, passNote.getSystem(), passNote.getUser(), passNote.getPassword(), passNote.getComments(), passNote.getCustom(), passNote.getInfo()));
        });
    }

    private PassCategory addCategoryData(Map<PassCategoryFX, PassCategory> categoryData, PassCategoryFX categoryFX) {
        PassCategory category = categoryData.get(categoryFX);
        if (category == null) {
            // create new system if not exists
            category = new PassCategory(categoryFX.getCategoryName());
            // create and set parent system
            PassCategoryFX parentCategoryFX = categoryFX.getParentCategory();
            if (parentCategoryFX != null) {
                PassCategory parentPassCategory = addCategoryData(categoryData, parentCategoryFX);
                category.setParentCategory(parentPassCategory);
            }
            categoryData.put(categoryFX, category);
        }
        return category;
    }

    public PassData saveNoteData() {
        PassData data = new PassData();
        List<PassCategory> passCategoryList = new ArrayList<>();
        List<PassNote> passNoteList = new ArrayList<>();
        data.setPassCategoryList(passCategoryList);
        data.setPassNoteList(passNoteList);

        Map<PassCategoryFX, PassCategory> categoryData = new HashMap<>();
        passCategoryData.stream().forEach((p) -> {
            addCategoryData(categoryData, p);
        });
        categoryData.entrySet().stream().forEach((p) -> {
            passCategoryList.add(p.getValue());
        });

        passNoteData.stream().forEach((p) -> {
            PassCategory category = categoryData.get(p.getCategory());
            PassNote note = new PassNote(category, p.getSystem(), p.getUser(), p.getPassword(), p.getComments(), p.getComments(), p.getInfo());
            passNoteList.add(note);
        });

        return data;
    }

    private void readDocument() {
        if (Document.getInstance().getPassData() != null) {
            loadCategoryData(Document.getInstance().getPassData().getPassCategoryList());
            loadNoteData(Document.getInstance().getPassData().getPassNoteList());
        }
    }

    private void writeDocument() {
        Document.getInstance().setPassData(saveNoteData());
    }

    public CategoryNotesModel() {
        readDocument();
    }

    public ObservableList<PassCategoryFX> getCategoryData() {
        return passCategoryData;
    }

    public ObservableList<PassNoteFX> getPassNoteData(PassCategoryFX category) {
        return new FilteredList<PassNoteFX>(passNoteData, p->{return p.getCategory().equals(category);});
    }


}
