package com.romanpulov.violetnotefx.categorynotes;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotefx.Document;
import com.romanpulov.violetnotefx.core.annotation.Model;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by 4540 on 22.02.2016.
 */
public class CategoryNotesPresenter implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(CategoryNotesPresenter.class);

    @FXML
    private TreeView<String> categoryTreeView;

    @FXML
    private TableView<CategoryNotesModel.PassNote> notesTableView;

    @FXML
    private Button categoryAddButton;

    @FXML
    private Button categoryDeleteButton;

    @FXML
    private Button categoryEditButton;

    @Model
    private CategoryNotesModel categoryNotesModel;

    private void loadTreeView() {
        TreeItem<String> root = new TreeItem<String>("Root Node");
        categoryTreeView.setRoot(root);
        categoryTreeView.setShowRoot(false);
        for (PassCategory passCategory : Document.getInstance().getPassData().getPassCategoryList()) {
            TreeItem<String> newItem = new TreeItem<>(passCategory.getCategoryName());
            root.getChildren().add(newItem);
        }

    }

    private void loadTable(String categoryName) {
        //PassCategory passCategory = Document.getInstance().getPassData().
        notesTableView.setItems(categoryNotesModel.getTableViewData(categoryName));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTreeView();

        //disable rules for tool buttons
        categoryDeleteButton.disableProperty().bind(categoryTreeView.getSelectionModel().selectedItemProperty().isNull());
        categoryEditButton.disableProperty().bind(categoryTreeView.getSelectionModel().selectedItemProperty().isNull());

        //selection change
        categoryTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                loadTable(newValue.getValue());
            }
        });
    }
}
