package com.romanpulov.violetnotefx.categorynotes;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by 4540 on 22.02.2016.
 */
public class CategoryNotesModel {

    public ObservableList<PassNote> getTableViewData(String categoryName) {
        ObservableList<PassNote> data = FXCollections.observableArrayList(
                new PassNote("System data", "user data", "password data", "comments data", "custom data", "info data")
        );
        return data;
    }

    public static class PassNote {
        private final SimpleStringProperty system;
        private final SimpleStringProperty user;
        private final SimpleStringProperty password;
        private final SimpleStringProperty comments;
        private final SimpleStringProperty custom;
        private final SimpleStringProperty info;

        public PassNote(String system, String user, String password, String comments, String custom, String info) {
            this.system = new SimpleStringProperty(system);
            this.user = new SimpleStringProperty(user);
            this.password = new SimpleStringProperty(password);
            this.comments = new SimpleStringProperty(comments);
            this.custom = new SimpleStringProperty(custom);
            this.info = new SimpleStringProperty(info);
        }

        public String getSystem() {
            return system.getValue();
        }
    }


}
