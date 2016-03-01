package com.romanpulov.violetnotefx.categorynotes;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotefx.AlertDialogs;
import com.romanpulov.violetnotefx.Document;
import com.romanpulov.violetnotefx.categoryname.CategoryNameStage;
import com.romanpulov.violetnotefx.core.annotation.Model;
import com.romanpulov.violetnotefx.note.NoteStage;
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

    @FXML
    private Button noteAddButton;

    @FXML
    private Button noteDeleteButton;

    @FXML
    private Button noteEditButton;

    @Model
    private CategoryNotesModel categoryNotesModel;

    private void loadTreeView() {
        TreeItem<CategoryNotesModel.PassCategoryFX> root = new TreeItem<>();
        categoryTreeView.setRoot(root);
        categoryTreeView.setShowRoot(false);
        categoryNotesModel.getCategoryData().stream().forEach((passCategoryFX -> {
            root.getChildren().add(new TreeItem<CategoryNotesModel.PassCategoryFX>(passCategoryFX));
        }));
    }

    private void loadTable(CategoryNotesModel.PassCategoryFX category) {
        notesTableView.setItems(categoryNotesModel.getPassNoteData(category));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTreeView();

        //simple disable rules for tool buttons
        categoryDeleteButton.disableProperty().bind(categoryTreeView.getSelectionModel().selectedItemProperty().isNull());
        categoryEditButton.disableProperty().bind(categoryTreeView.getSelectionModel().selectedItemProperty().isNull());

        noteAddButton.disableProperty().bind(categoryTreeView.getSelectionModel().selectedItemProperty().isNull());
        noteDeleteButton.disableProperty().bind(notesTableView.getSelectionModel().selectedItemProperty().isNull());
        noteEditButton.disableProperty().bind(notesTableView.getSelectionModel().selectedItemProperty().isNull());

        //selection change
        categoryTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<CategoryNotesModel.PassCategoryFX>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<CategoryNotesModel.PassCategoryFX>> observable, TreeItem<CategoryNotesModel.PassCategoryFX> oldValue, TreeItem<CategoryNotesModel.PassCategoryFX> newValue) {
                if (newValue != null)
                    loadTable(newValue.getValue());
                else
                    loadTable(null);
            }
        });

        // cell factory
        categoryTreeView.setCellFactory((tv) -> {
            return new TreeCell<CategoryNotesModel.PassCategoryFX>() {
                @Override
                protected void updateItem(CategoryNotesModel.PassCategoryFX item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.getDisplayValue());
                    }
                }
            };
        });
    }

    @FXML
    private void categoryAddButtonClick(ActionEvent event) {
        CategoryNameStage.CategoryNameData data = new CategoryNameStage.CategoryNameData();
        CategoryNameStage.showStage(data);
        if (data.modelResult == 0)
            return;

        if (categoryNotesModel.findChildPassCategoryName(null, data.categoryName) == null) {
            log.debug("can process add");
            categoryNotesModel.getCategoryData().add(new CategoryNotesModel.PassCategoryFX(null, data.categoryName));
            loadTreeView();
        } else {
            new AlertDialogs.ErrorAlertBuilder().setContentText("Category " + data.categoryName + " already exists").buildAlert().showAndWait();
        }
    }

    @FXML
    private void categoryDeleteButtonClick(ActionEvent event) {
        TreeItem<CategoryNotesModel.PassCategoryFX> selectedItem = categoryTreeView.getSelectionModel().getSelectedItem();
        if (notesTableView.getItems().size() > 0) {
            new AlertDialogs.ErrorAlertBuilder().setContentText("Unable to delete category containing items").buildAlert().showAndWait();
        }
        if (!(selectedItem.isLeaf())) {
            new AlertDialogs.ErrorAlertBuilder().setContentText("Node contains underlying nodes and cannot be deleted").buildAlert().showAndWait();
        }
        categoryNotesModel.getCategoryData().remove(selectedItem.getValue());
        loadTreeView();
    }

    @FXML
    private void categoryEditButtonClick(ActionEvent event) {
        CategoryNameStage.CategoryNameData data = new CategoryNameStage.CategoryNameData();
        TreeItem<CategoryNotesModel.PassCategoryFX> selectedTreeItem = categoryTreeView.getSelectionModel().getSelectedItem();
        CategoryNotesModel.PassCategoryFX selectedCategory = selectedTreeItem.getValue();
        data.categoryName = selectedCategory.getCategoryName();
        CategoryNameStage.showStage(data);
        if (data.modelResult == 0)
            return;

        if (categoryNotesModel.findChildPassCategoryName(selectedCategory.getParentCategory(), data.categoryName) != null) {
            new AlertDialogs.ErrorAlertBuilder().setContentText("Category " + data.categoryName + " already exists").buildAlert().showAndWait();
        } else {
            selectedCategory.setCategoryName(data.categoryName);
            // invalidate tree view
            selectedTreeItem.setValue(null);
            selectedTreeItem.setValue(selectedCategory);
        }
    }

    @FXML
    private void noteAddButtonClick(ActionEvent event) {
        CategoryNotesModel.PassCategoryFX categoryFX = categoryTreeView.getSelectionModel().getSelectedItem().getValue();
        CategoryNotesModel.PassNoteFX editNote = new CategoryNotesModel.PassNoteFX(categoryFX);
        NoteStage.showStage(editNote);
    }

    @FXML
    private void noteDeleteButtonClick(ActionEvent event) {

    }

    @FXML
    private void noteEditButtonClick(ActionEvent event) {
        CategoryNotesModel.PassNoteFX editNote = notesTableView.getSelectionModel().getSelectedItem();
        NoteStage.showStage(editNote);
    }

}