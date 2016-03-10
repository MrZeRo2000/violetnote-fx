package com.romanpulov.violetnotefx.Presentation.categorynotes;

import com.romanpulov.violetnotefx.Core.dialogs.AlertDialogs;
import com.romanpulov.violetnotefx.Model.Document;
import com.romanpulov.violetnotefx.Presentation.categoryname.CategoryNameStage;
import com.romanpulov.violetnotefx.Core.annotation.Model;
import com.romanpulov.violetnotefx.Model.PassCategoryFX;
import com.romanpulov.violetnotefx.Model.PassNoteFX;
import com.romanpulov.violetnotefx.Presentation.note.NoteStage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by 4540 on 22.02.2016.
 */
public class CategoryNotesPresenter implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(CategoryNotesPresenter.class);

    @FXML
    private AnchorPane rootContainer;

    @FXML
    private TreeView<PassCategoryFX> categoryTreeView;

    @FXML
    private TableView<PassNoteFX> notesTableView;

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

    @FXML
    private MenuItem fileSaveMenuItem;

    @Model
    private CategoryNotesModel categoryNotesModel;

    private void loadTreeView() {
        TreeItem<PassCategoryFX> root = categoryTreeView.getRoot();
        if (root == null) {
            root = new TreeItem<PassCategoryFX>();
            categoryTreeView.setRoot(root);
        } else {
            root.getChildren().clear();
        }
        categoryTreeView.setShowRoot(false);
        for (PassCategoryFX p : categoryNotesModel.getCategoryData()) {
            root.getChildren().add(new TreeItem<>(p));
        }
    }

    private void loadTable(PassCategoryFX category) {
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

        fileSaveMenuItem.disableProperty().bind(categoryNotesModel.getInvalidatedData().not());

        //selection change
        categoryTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<PassCategoryFX>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<PassCategoryFX>> observable, TreeItem<PassCategoryFX> oldValue, TreeItem<PassCategoryFX> newValue) {
                if (newValue != null)
                    loadTable(newValue.getValue());
                else
                    loadTable(null);
            }
        });

        // cell factory

        categoryTreeView.setCellFactory((tv) -> {
            return new TreeCell<PassCategoryFX>() {
                @Override
                protected void updateItem(PassCategoryFX item, boolean empty) {
                    super.updateItem(item, empty);
                    if ((empty) || (item == null)) {
                        setText(null);
                    } else {
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
        if (data.modalResult == ButtonType.CANCEL)
            return;

        if (categoryNotesModel.findChildPassCategoryName(null, data.categoryName) == null) {
            log.debug("can process add");
            categoryNotesModel.getCategoryData().add(new PassCategoryFX(null, data.categoryName));
            loadTreeView();
        } else {
            new AlertDialogs.ErrorAlertBuilder().setContentText("Category " + data.categoryName + " already exists").buildAlert().showAndWait();
        }
    }

    @FXML
    private void categoryDeleteButtonClick(ActionEvent event) {
        TreeItem<PassCategoryFX> selectedItem = categoryTreeView.getSelectionModel().getSelectedItem();
        if (notesTableView.getItems().size() > 0) {
            new AlertDialogs.ErrorAlertBuilder().setContentText("Unable to delete systemTextField_textProperty containing items").buildAlert().showAndWait();
            return;
        }
        if (!(selectedItem.isLeaf())) {
            new AlertDialogs.ErrorAlertBuilder().setContentText("Node contains underlying nodes and cannot be deleted").buildAlert().showAndWait();
            return;
        }
        categoryNotesModel.getCategoryData().remove(selectedItem.getValue());
        selectedItem.getParent().getChildren().remove(selectedItem);
        //loadTreeView();
    }

    @FXML
    private void categoryEditButtonClick(ActionEvent event) {
        CategoryNameStage.CategoryNameData data = new CategoryNameStage.CategoryNameData();
        TreeItem<PassCategoryFX> selectedTreeItem = categoryTreeView.getSelectionModel().getSelectedItem();
        PassCategoryFX selectedCategory = selectedTreeItem.getValue();
        data.categoryName = selectedCategory.getCategoryName();
        CategoryNameStage.showStage(data);
        if (data.modalResult == ButtonType.CANCEL)
            return;

        if (categoryNotesModel.findChildPassCategoryName(selectedCategory.getParentCategory(), data.categoryName) != null) {
            new AlertDialogs.ErrorAlertBuilder().setContentText("Category " + data.categoryName + " already exists").buildAlert().showAndWait();
        } else {
            selectedCategory.setCategoryName(data.categoryName);
            // invalidate tree view
            selectedTreeItem.setValue(null);
            selectedTreeItem.setValue(selectedCategory);
            categoryNotesModel.getInvalidatedData().setValue(true);
        }
    }

    @FXML
    private void noteAddButtonClick(ActionEvent event) {
        //create new note with systemTextField_textProperty
        PassCategoryFX categoryFX = categoryTreeView.getSelectionModel().getSelectedItem().getValue();
        PassNoteFX editNote = new PassNoteFX(categoryFX);

        NoteStage.NoteData data = new NoteStage.NoteData();
        data.passNoteFX = editNote;
        data.passCategoryData = categoryNotesModel.getPassCategoryData();
        NoteStage.showStage(data);
        if (data.modalResult == ButtonType.OK) {
            categoryNotesModel.getPassNoteData().add(data.passNoteFX);
        }
    }

    @FXML
    private void noteDeleteButtonClick(ActionEvent event) {
        Optional<ButtonType> result = new AlertDialogs.ConfirmationAlertBuilder().setContentText("Are you sure?").buildAlert().showAndWait();
        if (result.get() == ButtonType.OK) {
            categoryNotesModel.getPassNoteData().remove(notesTableView.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void noteEditButtonClick(ActionEvent event) {
        //get selected note
        PassNoteFX editNote = notesTableView.getSelectionModel().getSelectedItem();

        NoteStage.NoteData data = new NoteStage.NoteData();
        data.passNoteFX = PassNoteFX.newInstance(editNote);
        data.passCategoryData = categoryNotesModel.getPassCategoryData();
        NoteStage.showStage(data);
        if (data.modalResult == ButtonType.OK) {
            categoryNotesModel.getPassNoteData().set(categoryNotesModel.getPassNoteData().indexOf(editNote), data.passNoteFX);
        }
    }

    @FXML
    private void fileNewMenuItemClick(ActionEvent event) {
        log.debug("File New menu item click");
        categoryNotesModel.initData();
        loadTreeView();
        notesTableView.setItems(null);
        Document.getInstance().resetFileName();
    }

    @FXML
    private void fileOpenMenuItemClick(ActionEvent event) {
        log.debug("File Open menu item click");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Violetnote file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Violetnote files", "*.vnf"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        File f = fileChooser.showOpenDialog(rootContainer.getScene().getWindow());
        if (f != null) {
            log.debug("Something chosen: " + f.getPath());
        }
    }

    @FXML
    private void fileSaveMenuItemClick(ActionEvent event) {
        if (Document.getInstance().isNewFile()) {
            fileSaveAsMenuItemClick(event);
        } else {
            File f = new File(Document.getInstance().getFileName().getValue());
            if (categoryNotesModel.loadFile(f)) {
                categoryNotesModel.getInvalidatedData().setValue(false);
                Document.getInstance().getFileName().setValue(f.getName());
                loadTreeView();
            }
        }
    }

    @FXML
    private void fileSaveAsMenuItemClick(ActionEvent event) {
        log.debug("File Save menu item click");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Violetnote file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Violetnote files", "*.vnf")
        );
        File f = fileChooser.showSaveDialog(rootContainer.getScene().getWindow());
        if (f != null) {
            log.debug("Something chosen: " + f.getPath());
            if (categoryNotesModel.saveFile(f)) {
                categoryNotesModel.getInvalidatedData().setValue(false);
            }
        }
    }

    @FXML
    private void fileImportMenuItemClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import from PINS");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV files", "*.csv"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        File f = fileChooser.showOpenDialog(rootContainer.getScene().getWindow());
        if (f != null) {
            log.debug("Something chosen: " + f.getPath());
            if (categoryNotesModel.importPINSFile(f)) {
                categoryNotesModel.getInvalidatedData().setValue(true);
                loadTreeView();
            }
        }
    }
}