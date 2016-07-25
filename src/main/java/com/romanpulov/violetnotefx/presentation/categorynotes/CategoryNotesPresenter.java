package com.romanpulov.violetnotefx.Presentation.categorynotes;

import com.romanpulov.violetnotefx.Core.dialogs.AlertDialogs;
import com.romanpulov.violetnotefx.Core.presentation.ProgressNode;
import com.romanpulov.violetnotefx.Model.Document;
import com.romanpulov.violetnotefx.Presentation.DialogsHelper;
import com.romanpulov.violetnotefx.Presentation.categoryname.CategoryNameModel;
import com.romanpulov.violetnotefx.Presentation.categoryname.CategoryNameStage;
import com.romanpulov.violetnotefx.Core.annotation.Model;
import com.romanpulov.violetnotefx.Model.PassCategoryFX;
import com.romanpulov.violetnotefx.Model.PassNoteFX;
import com.romanpulov.violetnotefx.Presentation.masterpass.MasterPassStage;
import com.romanpulov.violetnotefx.Presentation.note.NoteModel;
import com.romanpulov.violetnotefx.Presentation.note.NoteStage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.function.Function;

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
    private Button noteDuplicateButton;

    @FXML
    private Button notePasswordToClipboardButton;

    @FXML
    private MenuItem fileSaveMenuItem;

    @FXML
    private Button fileSaveButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchButton;

    @Model
    private CategoryNotesModel categoryNotesModel;

    private ProgressNode progressNode;

    private void showProgressNode(String text) {
        hideProgressNode();
        progressNode = ProgressNode.newInstance().setParentScene(rootContainer.getScene()).setText(text);
        progressNode.show();
    }

    private void hideProgressNode() {
        if (progressNode != null) {
            progressNode.hide();
            progressNode = null;
        }
    }

    public void updateController() {
        loadTreeView();
    }

    private void loadTreeView() {
        /*
        TreeItem<PassCategoryFX> root = categoryTreeView.getRoot();
        if (root == null) {
            root = new TreeItem<PassCategoryFX>();
            categoryTreeView.setRoot(root);
        } else {
            categoryTreeView.getSelectionModel().clearSelection();
            root.getChildren().clear();
        }
        */
        TreeItem<PassCategoryFX> root = new TreeItem<>();
        categoryTreeView.setRoot(root);
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
        noteDuplicateButton.disableProperty().bind(notesTableView.getSelectionModel().selectedItemProperty().isNull());
        notePasswordToClipboardButton.disableProperty().bind(notesTableView.getSelectionModel().selectedItemProperty().isNull());

        fileSaveMenuItem.disableProperty().bind(categoryNotesModel.getInvalidatedData().not());
        fileSaveButton.disableProperty().bind(categoryNotesModel.getInvalidatedData().not());

        //selection change
        categoryTreeView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null)
                loadTable(newValue.getValue());
            else
                loadTable(null);
        }));

        // cell factory

        categoryTreeView.setCellFactory((tv) ->
            new TreeCell<PassCategoryFX>() {
                @Override
                protected void updateItem(PassCategoryFX item, boolean empty) {
                    super.updateItem(item, empty);
                    if ((empty) || (item == null)) {
                        setText(null);
                    } else {
                        setText(item.getDisplayValue());
                    }
                }
        });

        // double click handling
        notesTableView.setOnMouseClicked((event) -> {
            if ((event != null) &&  (event.getClickCount() == 2))
                noteEditButtonClick(null);
        });

        categoryTreeView.setOnMouseClicked((event) -> {
            if ((event != null) &&  (event.getClickCount() == 2))
                categoryEditButtonClick(null);
        });

        //search
        searchTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                log.debug("search field changed focus to " + searchTextField.getScene().getFocusOwner());
                handleSearchFocusChange(searchTextField.getScene().getFocusOwner());
            }
        });

        searchButton.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                log.debug("search button changed focus to " + searchTextField.getScene().getFocusOwner());
                handleSearchFocusChange(searchTextField.getScene().getFocusOwner());
            }
        });
    }

    private boolean checkUnsavedData() {
        return (!categoryNotesModel.getInvalidatedData().getValue() || (categoryNotesModel.getInvalidatedData().getValue()) && (DialogsHelper.queryUnsavedData()));
    }

    @FXML
    private void categoryAddButtonClick(ActionEvent event) {
        CategoryNameStage categoryNameStage = new CategoryNameStage();
        categoryNameStage.createStage();
        CategoryNameModel categoryNameModel = categoryNameStage.getModel();
        categoryNameStage.showModal();

        if (categoryNameModel.modalResult == ButtonType.CANCEL)
            return;

        if (categoryNotesModel.findChildPassCategoryName(null, categoryNameModel.categoryName.getValue()) == null) {
            categoryNotesModel.getCategoryData().add(new PassCategoryFX(null, categoryNameModel.categoryName.getValue()));
            loadTreeView();
        } else {
            new AlertDialogs.ErrorAlertBuilder().setContentText("Category " + categoryNameModel.categoryName.getValue() + " already exists").buildAlert().showAndWait();
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
        loadTreeView();
    }

    @FXML
    private void categoryEditButtonClick(ActionEvent event) {
        // find selected category
        TreeItem<PassCategoryFX> selectedTreeItem = categoryTreeView.getSelectionModel().getSelectedItem();
        PassCategoryFX selectedCategory = selectedTreeItem.getValue();

        CategoryNameStage categoryNameStage = new CategoryNameStage();
        categoryNameStage.createStage();
        CategoryNameModel categoryNameModel = categoryNameStage.getModel();
        categoryNameModel.categoryName.setValue(selectedCategory.getCategoryName());
        categoryNameStage.showModal();

        if (categoryNameModel.modalResult == ButtonType.CANCEL)
            return;

        if (categoryNotesModel.findChildPassCategoryName(selectedCategory.getParentCategory(), categoryNameModel.categoryName.getValue()) != null) {
            new AlertDialogs.ErrorAlertBuilder().setContentText("Category " + categoryNameModel.categoryName.getValue() + " already exists").buildAlert().showAndWait();
        } else {
            selectedCategory.setCategoryName(categoryNameModel.categoryName.getValue());
            // invalidate tree view
            selectedTreeItem.setValue(null);
            selectedTreeItem.setValue(selectedCategory);
            categoryNotesModel.getInvalidatedData().setValue(true);
        }
    }

    @FXML
    private void noteAddButtonClick(ActionEvent event) {
        // setup stage and category data
        NoteStage noteStage = new NoteStage();
        noteStage.createStage();
        NoteModel noteModel = noteStage.getModel();
        noteModel.setPassCategoryData(categoryNotesModel.getPassCategoryData());

        PassCategoryFX categoryFX = categoryTreeView.getSelectionModel().getSelectedItem().getValue();
        noteModel.setPassNoteFX(PassNoteFX.newEmptyInstance(categoryFX));

        noteStage.showModal();

        if (noteModel.modalResult == ButtonType.OK) {
            categoryNotesModel.getPassNoteData().add(noteModel.getPassNoteFX());
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
        PassNoteFX editNote = notesTableView.getSelectionModel().getSelectedItem();
        if (editNote != null) {
            // setup stage and category data
            NoteStage noteStage = new NoteStage();
            noteStage.createStage();
            NoteModel noteModel = noteStage.getModel();
            noteModel.setPassCategoryData(categoryNotesModel.getPassCategoryData());
            noteModel.setPassNoteFX(PassNoteFX.newInstance(editNote));

            noteStage.showModal();

            if (noteModel.modalResult == ButtonType.OK) {
                categoryNotesModel.getPassNoteData().set(categoryNotesModel.getPassNoteData().indexOf(editNote), noteModel.getPassNoteFX());
            }
        }
    }

    @FXML
    private void noteDuplicateButtonClick(ActionEvent event) {
        // setup stage and category data
        NoteStage noteStage = new NoteStage();
        noteStage.createStage();
        NoteModel noteModel = noteStage.getModel();
        noteModel.setPassCategoryData(categoryNotesModel.getPassCategoryData());

        PassNoteFX editNote = notesTableView.getSelectionModel().getSelectedItem();
        noteModel.setPassNoteFX(PassNoteFX.newDuplicateInstance(editNote));

        noteStage.showModal();

        if (noteModel.modalResult == ButtonType.OK) {
            categoryNotesModel.getPassNoteData().add(noteModel.getPassNoteFX());
        }
    }

    @FXML
    private void notePasswordToClipboardButtonClick(ActionEvent event) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(notesTableView.getSelectionModel().getSelectedItem().getRealPassword());
        clipboard.setContent(content);
    }

    @FXML
    private void fileNewMenuItemClick(ActionEvent event) {
        if (checkUnsavedData()) {
            categoryNotesModel.initData();
            loadTreeView();
            notesTableView.setItems(null);
            Document.getInstance().resetFileName();
        }
    }

    public void loadVNF(File f) {
        String masterPass = MasterPassStage.queryMasterPass(f, null);
        if ((masterPass != null) && (categoryNotesModel.loadFile(f, masterPass))) {
            loadTreeView();
        } else {
            (new AlertDialogs.ErrorAlertBuilder()).setHeaderText("Error reading file: " + f.getPath()).setTitle("Error").setContentText("Wrong password or invalid file").buildAlert().showAndWait();
        }
    }

    public void importPINS(File f) {
        if (categoryNotesModel.importPINSFile(f)) {
            loadTreeView();
        }
    }

    public void exportPINS(File f) {
        categoryNotesModel.exportPINSFile(f);
    }

    @FXML
    private void fileOpenMenuItemClick(ActionEvent event) {
        if (!checkUnsavedData())
            return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Violetnote file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Violetnote files", "*.vnf"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        File f = fileChooser.showOpenDialog(rootContainer.getScene().getWindow());
        if (f != null) {
            loadVNF(f);
        }
    }

    @FXML
    private void fileSaveMenuItemClick(ActionEvent event) {
        if (Document.getInstance().isNewFile()) {
            fileSaveAsMenuItemClick(event);
        } else {
            File f = new File(Document.getInstance().getFileName().getValue());

            String masterPass = Document.getInstance().getMasterPass();
            if (masterPass == null)
                masterPass = MasterPassStage.queryMasterPass(f, null);

            if ((masterPass != null) && (categoryNotesModel.saveFile(f, masterPass))) {
                loadTreeView();
            }
        }
    }

    @FXML
    private void fileSaveAsMenuItemClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Violetnote file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Violetnote files", "*.vnf")
        );
        File f = fileChooser.showSaveDialog(rootContainer.getScene().getWindow());
        if (f != null) {
            String masterPass = MasterPassStage.queryMasterPass(f, null);
            if (masterPass != null) {
                categoryNotesModel.saveFile(f, masterPass);
            }
        }
    }

    @FXML
    private void fileImportMenuItemClick(ActionEvent event) {
        if (!checkUnsavedData())
            return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import from PINS");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV files", "*.csv"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        File f = fileChooser.showOpenDialog(rootContainer.getScene().getWindow());
        if (f != null) {
            importPINS(f);
        }
    }

    @FXML
    private void fileExportMenuItemClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export to PINS");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV files", "*.csv"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        File f = fileChooser.showSaveDialog(rootContainer.getScene().getWindow());
        if (f != null) {
            exportPINS(f);
        }
    }

    private TreeItem<PassCategoryFX> findTreeViewItem(TreeItem<PassCategoryFX> root, PassCategoryFX passCategoryFX) {
        for (TreeItem<PassCategoryFX> p : root.getChildren()) {
            if (p.getValue().equals(passCategoryFX))
                return p;
            else if (!p.isLeaf()) {
                TreeItem<PassCategoryFX> res = findTreeViewItem(p, passCategoryFX);
                if (res != null)
                    return res;
            }
        }
        return null;
    }

    private void selectPassNoteFX(PassNoteFX passNoteFX) {
        TreeItem<PassCategoryFX> selectedItem = findTreeViewItem(categoryTreeView.getRoot(), passNoteFX.getCategory());
        if (selectedItem != null) {
            //select category
            categoryTreeView.getSelectionModel().select(selectedItem);
            //select note
            notesTableView.getSelectionModel().select(passNoteFX);
            //make selected note visible
            notesTableView.scrollTo(passNoteFX);
        }

        //categoryTreeView.getSelectionModel().select();
        //notesTableView.getSelectionModel().select(passNoteFX);
    }

    private void performSearch() {
        String searchText = searchTextField.getText();
        if ((searchText == null) || (searchText.isEmpty())) {
            categoryNotesModel.clearSearch();
        }
        else {
            PassNoteFX searchNoteFX = categoryNotesModel.requestSearch(searchText);
            if (searchNoteFX != null) {
                selectPassNoteFX(searchNoteFX);
            }
        }
    }

    public void activateSearch() {
        searchTextField.requestFocus();
    }

    private void handleSearchFocusChange(Node focusedNode) {
        if ((focusedNode != searchButton) && (focusedNode != searchTextField)) {
            categoryNotesModel.clearSearch();
        }
    }

    @FXML
    private void searchAction(ActionEvent event) {
        performSearch();
    }
}