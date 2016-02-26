package com.romanpulov.violetnotefx.categorynotes;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotefx.Document;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
            if (p.sourcePassCategory.equals(passCategory))
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
            // create new category if not exists
            category = new PassCategory(categoryFX.getCategoryName());
            // create and set parent category
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
        return new FilteredList<PassNoteFX>(passNoteData, p->{return p.category.get().equals(category);});
    }

    public static class PassCategoryFX {
        private PassCategory sourcePassCategory;
        private final SimpleObjectProperty<PassCategoryFX> parentCategory;
        private final SimpleStringProperty categoryName;

        public PassCategoryFX(PassCategoryFX parentCategory, String categoryName) {
            this.parentCategory = new SimpleObjectProperty<>(parentCategory);
            this.categoryName = new SimpleStringProperty(categoryName);
        }

        public void setSourcePassCategory(PassCategory passCategory) {
            sourcePassCategory = passCategory;
        }

        public String getCategoryName() {
            return categoryName.get();
        }

        public void setCategoryName(String value) {
            categoryName.setValue(value);
        }

        public PassCategoryFX getParentCategory() {
            return parentCategory.get();
        }

        @Override
        public String toString(){
            return "{parentCategory = " + parentCategory.get() + ", categoryName = " + categoryName.get() + "}";
        }
    }

    public static class PassNoteFX {
        private final SimpleObjectProperty<PassCategoryFX> category;
        //private final SimpleStringProperty categoryName;
        private final SimpleStringProperty system;
        private final SimpleStringProperty user;
        private final SimpleStringProperty password;
        private final SimpleStringProperty comments;
        private final SimpleStringProperty custom;
        private final SimpleStringProperty info;

        public PassNoteFX(PassCategoryFX category, String system, String user, String password, String comments, String custom, String info) {
            this.category = new SimpleObjectProperty<PassCategoryFX>(category);
            this.system = new SimpleStringProperty(system);
            this.user = new SimpleStringProperty(user);
            this.password = new SimpleStringProperty(password);
            this.comments = new SimpleStringProperty(comments);
            this.custom = new SimpleStringProperty(custom);
            this.info = new SimpleStringProperty(info);
        }

        public String getCategoryName() {
            return (category.get() == null) ? null : category.get().getCategoryName();
        }

        public PassCategoryFX getCategory() {
            return category.get();
        }

        public String getSystem() {
            return system.get();
        }

        public String getUser() {
            return user.get();
        }

        public String getPassword() {
            return new String(new char[password.get().length()]).replace("\0", "*");
        }

        public String getComments() {
            return comments.get();
        }

        public String getCustom() {
            return custom.get();
        }

        public String getInfo() {
            return info.get();
        }

        @Override
        public String toString() {
            return "{category = " + getCategory() + ", system = " + getSystem() + ", user = " + getUser() + ", password = " + getPassword() +
                    ", comments = " + getComments() + ", custom = " + getCustom() + ", info = " + getInfo() + "}";
        }

    }


}
