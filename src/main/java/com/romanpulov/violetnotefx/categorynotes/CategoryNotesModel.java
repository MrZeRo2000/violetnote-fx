package com.romanpulov.violetnotefx.categorynotes;

import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotefx.Document;
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

    private void readDocument() {
        passCategoryData = FXCollections.observableArrayList();
        Document.getInstance().getPassData().getPassCategoryList().stream().forEach((passCategory -> {
            passCategoryData.add(new PassCategoryFX(passCategory.getCategoryName()));
        }));
        passNoteData =  FXCollections.observableArrayList();
        Document.getInstance().getPassData().getPassNoteList().stream().forEach((passNote)-> {
            passNoteData.add(new PassNoteFX(passNote.getPassCategory().getCategoryName(), passNote.getSystem(), passNote.getUser(), passNote.getPassword(), passNote.getComments(), passNote.getCustom(), passNote.getInfo()));
        });
    }

    public CategoryNotesModel() {
        readDocument();
    }

    public ObservableList<PassCategoryFX> getCategoryData() {
        return passCategoryData;
    }

    public ObservableList<PassNoteFX> getPassNoteData(String categoryName) {
        FilteredList<PassNoteFX> filteredPassNoteData = new FilteredList<PassNoteFX>(passNoteData, p->{return p.categoryName.get().equals(categoryName);});
        return filteredPassNoteData;
    }

    public static class PassCategoryFX {
        private final SimpleStringProperty categoryName;

        public PassCategoryFX(String categoryName) {
            this.categoryName = new SimpleStringProperty(categoryName);
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
        private final SimpleStringProperty categoryName;
        private final SimpleStringProperty system;
        private final SimpleStringProperty user;
        private final SimpleStringProperty password;
        private final SimpleStringProperty comments;
        private final SimpleStringProperty custom;
        private final SimpleStringProperty info;

        public PassNoteFX(String categoryName, String system, String user, String password, String comments, String custom, String info) {
            this.categoryName = new SimpleStringProperty(categoryName);
            this.system = new SimpleStringProperty(system);
            this.user = new SimpleStringProperty(user);
            this.password = new SimpleStringProperty(password);
            this.comments = new SimpleStringProperty(comments);
            this.custom = new SimpleStringProperty(custom);
            this.info = new SimpleStringProperty(info);
        }

        public String getCategoryName() {
            return categoryName.get();
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
