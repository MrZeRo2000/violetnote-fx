package com.romanpulov.violetnotefx.categorynotes;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotefx.Document;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Created by 4540 on 22.02.2016.
 */
public class CategoryNotesModel {

    private ObservableList<PassNoteFX> passNoteData;
    private ObservableList<PassCategoryFX> passCategoryData;

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

    private void readDocument() {
        passCategoryData = FXCollections.observableArrayList();

        Document.getInstance().getPassData().getPassCategoryList().stream().forEach((passCategory -> {
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

        passNoteData =  FXCollections.observableArrayList();
        Document.getInstance().getPassData().getPassNoteList().stream().forEach((passNote)-> {
            PassCategoryFX passCategoryFX = findSourcePassCategory(passNote.getPassCategory());
            if (passCategoryFX != null)
                passNoteData.add(new PassNoteFX(passCategoryFX, passNote.getSystem(), passNote.getUser(), passNote.getPassword(), passNote.getComments(), passNote.getCustom(), passNote.getInfo()));
        });
    }

    public CategoryNotesModel() {
        readDocument();
    }

    public ObservableList<PassCategoryFX> getCategoryData() {
        return passCategoryData;
    }

    public ObservableList<PassNoteFX> getPassNoteData(PassCategoryFX category) {
        FilteredList<PassNoteFX> filteredPassNoteData = new FilteredList<PassNoteFX>(passNoteData, p->{return p.category.get().equals(category);});
        return filteredPassNoteData;
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

        @Override
        public String toString(){
            return categoryName.get();
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
            this.category = new SimpleObjectProperty(category);
            this.system = new SimpleStringProperty(system);
            this.user = new SimpleStringProperty(user);
            this.password = new SimpleStringProperty(password);
            this.comments = new SimpleStringProperty(comments);
            this.custom = new SimpleStringProperty(custom);
            this.info = new SimpleStringProperty(info);
        }

        public String getCategoryName() {
            return category.get().getCategoryName();
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

        public String getInfo() {
            return info.get();
        }

    }


}
