package com.romanpulov.violetnotefx.categorynotes;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotefx.Document;
import com.romanpulov.violetnotefx.categoryname.CategoryNameStage;
import com.romanpulov.violetnotefx.core.annotation.Model;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by 4540 on 22.02.2016.
 */
public class CategoryNotesPresenter implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(CategoryNotesPresenter.class);

    @FXML
    private TreeView<CategoryNotesModel.PassCategoryFX> categoryTreeView;

    @FXML
    private TableView<CategoryNotesModel.PassNoteFX> notesTableView;

    @FXML
    private Button categoryAddButton;

    @FXML
    private Button categoryDeleteButton;

    @FXML
    private Button categoryEditButton;

    @Model
    private CategoryNotesModel categoryNotesModel;

    private void loadTreeView() {
        TreeItem<CategoryNotesModel.PassCategoryFX> root = new TreeItem<>();
        categoryTreeView.setRoot(root);
        categoryTreeView.setShowRoot(false);
        categoryNotesModel.getCategoryData().stream().forEach((passCategoryFX -> {
            root.getChildren().add(new TreeItem<>(passCategoryFX));
        }));
    }

    private void loadTable(CategoryNotesModel.PassCategoryFX category) {
        notesTableView.setItems(categoryNotesModel.getPassNoteData(category));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTreeView();

        //disable rules for tool buttons
        categoryDeleteButton.disableProperty().bind(categoryTreeView.getSelectionModel().selectedItemProperty().isNull());
        categoryEditButton.disableProperty().bind(categoryTreeView.getSelectionModel().selectedItemProperty().isNull());

        //selection change
        categoryTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<CategoryNotesModel.PassCategoryFX>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<CategoryNotesModel.PassCategoryFX>> observable, TreeItem<CategoryNotesModel.PassCategoryFX> oldValue, TreeItem<CategoryNotesModel.PassCategoryFX> newValue) {
                loadTable(newValue.getValue());
            }
        });
    }

    @FXML
    private void categoryAddButtonClick(ActionEvent event) {
        log.debug("add clicked");
        CategoryNameStage.CategoryNameData data = new CategoryNameStage.CategoryNameData();
        data.categoryName = "Init stuff";
        CategoryNameStage.showStage(data);
        log.debug("Input category data = " + data);

        if (categoryNotesModel.findChildPassCategoryName(null, data.categoryName) == null) {
            log.debug("can process add");
            categoryNotesModel.getCategoryData().add(new CategoryNotesModel.PassCategoryFX(null, data.categoryName));
            loadTreeView();
        } else {

        }

            log.debug("category exists");

    }

}
