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
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 4540 on 22.02.2016.
 */
public class CategoryNotesPresenter implements Initializable {
    private static final Logger log = Logger.getLogger(CategoryNotesPresenter.class);

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
    private Button categoryMoveUpButton;

    @FXML
    private Button categoryMoveDownButton;

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
    private Button noteMoveUpButton;

    @FXML
    private Button noteMoveDownButton;

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

    private final IntegerProperty treeViewLastItemIndexProperty = new SimpleIntegerProperty(0);
    private IntegerProperty notesTableLastItemIndexProperty = new SimpleIntegerProperty(0);
    private StringProperty treeViewSelectedCategoryNameProperty = new SimpleStringProperty();
    private final IntegerProperty treeViewSelectedCountProperty = new SimpleIntegerProperty(0);

    private ProgressNode progressNode;

    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    public void shutdownExecutorService() {
        executorService.shutdown();
    }

    private void showProgressNode(String text) {
        log.debug("Showing progress, scene = " + rootContainer.getScene());
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

        int lastItemIndex = -1;
        for (PassCategoryFX p : categoryNotesModel.getCategoryData()) {
            root.getChildren().add(new TreeItem<>(p));
            lastItemIndex ++;
        }
        treeViewLastItemIndexProperty.setValue(lastItemIndex);
    }

    private void setTreeViewCellFactory() {
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
    }

    private void loadTable(PassCategoryFX category) {
        notesTableView.setItems(categoryNotesModel.getPassNoteData(category));
        notesTableLastItemIndexProperty.setValue(notesTableView.getItems().size() - 1);
        notesTableView.contextMenuProperty().bind(
                Bindings.when(notesTableView.getSelectionModel().selectedItemProperty().isNull()).then((ContextMenu)null).otherwise(createTableContextMenu())
        );
    }

    private ContextMenu createTableContextMenu() {
        final ContextMenu contextMenu = new ContextMenu();
        final Menu moveMenu = new Menu("Move to category");

        //add last
        PassCategoryFX lastPassCategoryFX = categoryNotesModel.findChildPassCategoryName(null, categoryNotesModel.getLastUpdateCategoryName());
        if (lastPassCategoryFX != null) {
            MenuItem lastPassCategoryMenuItem = createCategoryMenuItem(lastPassCategoryFX);
            moveMenu.getItems().add(lastPassCategoryMenuItem);
        }

        categoryNotesModel.getPassCategoryData().filtered(passCategoryFX -> !passCategoryFX.equals(lastPassCategoryFX)).forEach(passCategoryFX -> {
            MenuItem categoryMenuItem = createCategoryMenuItem(passCategoryFX);
            moveMenu.getItems().add(categoryMenuItem);
        });

        contextMenu.getItems().add(moveMenu);

        return contextMenu;
    }

    /*
    // another way to implement context menu for table
    private void setTableRowFactory() {
        notesTableView.setRowFactory(tv -> {
            final TableRow<PassNoteFX> row = new TableRow<>();
            final ContextMenu contextMenu = new ContextMenu();
            final Menu moveMenu = new Menu("Move to category");

            //add last
            PassCategoryFX lastPassCategoryFX = categoryNotesModel.findChildPassCategoryName(null, categoryNotesModel.getLastUpdateCategoryName());
            if (lastPassCategoryFX != null) {
                MenuItem lastPassCategoryMenuItem = createCategoryMenuItem(lastPassCategoryFX);
                moveMenu.getItems().add(lastPassCategoryMenuItem);
            }

            categoryNotesModel.getPassCategoryData().filtered(passCategoryFX -> !passCategoryFX.equals(lastPassCategoryFX)).forEach(passCategoryFX -> {
                MenuItem categoryMenuItem = createCategoryMenuItem(passCategoryFX);
                moveMenu.getItems().add(categoryMenuItem);
            });


            contextMenu.getItems().add(moveMenu);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(contextMenu)
            );
            return row;
        });
    }
    */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTreeView();

        //simple disable rules for tool buttons
        categoryDeleteButton.disableProperty().bind(categoryTreeView.getSelectionModel().selectedItemProperty().isNull());
        categoryEditButton.disableProperty().bind(categoryTreeView.getSelectionModel().selectedItemProperty().isNull());

        //move up and down
        categoryMoveUpButton.disableProperty().bind(
                categoryTreeView.getSelectionModel().selectedItemProperty().isNull().or(
                categoryTreeView.getSelectionModel().selectedIndexProperty().isEqualTo(0)));
        categoryMoveDownButton.disableProperty().bind(
                categoryTreeView.getSelectionModel().selectedItemProperty().isNull().or(
                        categoryTreeView.getSelectionModel().selectedIndexProperty().isEqualTo(treeViewLastItemIndexProperty)));



        noteMoveUpButton.disableProperty().bind(
                treeViewSelectedCountProperty.isNotEqualTo(1).or(
                        notesTableView.getSelectionModel().selectedIndexProperty().isEqualTo(0)
                )
        );
        noteMoveDownButton.disableProperty().bind(
                treeViewSelectedCountProperty.isNotEqualTo(1).or(
                        notesTableView.getSelectionModel().selectedIndexProperty().isEqualTo(notesTableLastItemIndexProperty)
                )
        );

        //
        noteAddButton.disableProperty().bind(categoryTreeView.getSelectionModel().selectedItemProperty().isNull());
        noteDeleteButton.disableProperty().bind(notesTableView.getSelectionModel().selectedItemProperty().isNull());
        noteEditButton.disableProperty().bind(treeViewSelectedCountProperty.isNotEqualTo(1));

        noteDuplicateButton.disableProperty().bind(treeViewSelectedCountProperty.isNotEqualTo(1));
        notePasswordToClipboardButton.disableProperty().bind(treeViewSelectedCountProperty.isNotEqualTo(1));

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
        setTreeViewCellFactory();

        //setTableRowFactory();

        categoryTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                treeViewSelectedCategoryNameProperty.setValue(newValue.getValue().getCategoryName());
        });

        // double click handling
        notesTableView.setOnMouseClicked((event) -> {
            PassNoteFX editNote;
            if ((event != null) &&
                    (event.getClickCount() == 2) &&
                    ((editNote = notesTableView.getSelectionModel().getSelectedItem()) != null)) {
                NoteStage.createReadOnly(editNote).showModal();
            }
        });


        //notesTableView.getSelectionModel().selectedItemProperty()

        //enter key
        notesTableView.setOnKeyPressed((event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                PassNoteFX editNote;
                if ((editNote = notesTableView.getSelectionModel().getSelectedItem()) != null) {
                    NoteStage.createReadOnly(editNote).showModal();
                }
            }
        }));

        notesTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        notesTableView.getSelectionModel().getSelectedCells().addListener((ListChangeListener.Change<? extends TablePosition> c) -> {
            treeViewSelectedCountProperty.setValue(notesTableView.getSelectionModel().getSelectedCells().size());
            log.debug("Selected " + notesTableView.getSelectionModel().getSelectedCells().size() + " items in tableView");
        });

        categoryTreeView.setOnMouseClicked((event) -> {
            PassCategoryFX editCategory;
            if ((event != null) &&  (event.getClickCount() == 2) && ((editCategory = categoryTreeView.getSelectionModel().getSelectedItem().getValue()) != null))
                //categoryEditButtonClick(null);
                CategoryNameStage.createReadOnly(editCategory).showModal();
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

    private MenuItem createCategoryMenuItem(PassCategoryFX passCategoryFX) {
        MenuItem categoryMenuItem = new MenuItem(passCategoryFX.getCategoryName());
        categoryMenuItem.setUserData(passCategoryFX);
        categoryMenuItem.visibleProperty().bind(
                treeViewSelectedCategoryNameProperty.isNotEqualTo(categoryMenuItem.textProperty())
        );
        categoryMenuItem.setOnAction(event -> {
            Object userData = ((MenuItem) event.getSource()).getUserData();
            if (userData instanceof PassCategoryFX) {
                PassCategoryFX selectedCategory = (PassCategoryFX) userData;
                List<PassNoteFX> selectedList = notesTableView.getSelectionModel().getSelectedItems();

                if ((selectedList != null) && (selectedList.size() > 0)) {
                    PassCategoryFX oldSelectedCategory = selectedList.get(0).getCategory();

                    categoryNotesModel.notesUpdateCategory(selectedList, selectedCategory);

                    loadTreeView();

                    //to navigate to new position
                    Optional<TreeItem<PassCategoryFX>> categoryItem =
                            categoryTreeView.getRoot().getChildren().stream().filter(
                                    obj -> obj.getValue().equals(oldSelectedCategory)
                            ).findFirst();

                    categoryItem.ifPresent((treeItem) -> categoryTreeView.getSelectionModel().select(treeItem));
                }
            }
        });

        return categoryMenuItem;
    }

    private boolean checkUnsavedData() {
        return (!categoryNotesModel.getInvalidatedData().getValue() || (categoryNotesModel.getInvalidatedData().getValue()) && (DialogsHelper.queryUnsavedData()));
    }

    @FXML
    private void categoryAddButtonClick(ActionEvent event) {
        CategoryNameStage categoryNameStage = CategoryNameStage.createForAdd(null);
        CategoryNameModel categoryNameModel = categoryNameStage.getModel();
        categoryNameStage.showModal();

        if (categoryNameModel.modalResult == ButtonType.CANCEL)
            return;

        String categoryName = categoryNameModel.getPassCategoryFX().getCategoryName();

        if (categoryNotesModel.findChildPassCategoryName(null, categoryName) == null) {
            categoryNotesModel.getCategoryData().add(new PassCategoryFX(null, categoryName));
            loadTreeView();
        } else {
            new AlertDialogs.ErrorAlertBuilder().setContentText("Category " + categoryName + " already exists").buildAlert().showAndWait();
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


        CategoryNameStage categoryNameStage = CategoryNameStage.createForEdit(null, selectedCategory);
        CategoryNameModel categoryNameModel = categoryNameStage.getModel();
        categoryNameStage.showModal();

        if (categoryNameModel.modalResult == ButtonType.CANCEL)
            return;

        String categoryName = categoryNameModel.getPassCategoryFX().getCategoryName();

        if (categoryNotesModel.findChildPassCategoryName(selectedCategory.getParentCategory(), categoryName) != null) {
            new AlertDialogs.ErrorAlertBuilder().setContentText("Category " + categoryName + " already exists").buildAlert().showAndWait();
        } else {
            selectedCategory.setCategoryName(categoryName);
            // invalidate tree view
            selectedTreeItem.setValue(null);
            selectedTreeItem.setValue(selectedCategory);
            categoryNotesModel.getInvalidatedData().setValue(true);
        }
    }

    @FXML
    private void noteAddButtonClick(ActionEvent event) {
        NoteStage noteStage = NoteStage.createForAdd(
                categoryNotesModel.getPassCategoryData(),
                categoryTreeView.getSelectionModel().getSelectedItem().getValue());
        NoteModel noteModel = noteStage.getModel();
        noteStage.showModal();

        if (noteModel.modalResult == ButtonType.OK) {
            categoryNotesModel.getPassNoteData().add(noteModel.getPassNoteFX());
        }
    }

    @FXML
    private void noteDeleteButtonClick(ActionEvent event) {
        Optional<ButtonType> result = new AlertDialogs.ConfirmationAlertBuilder().setContentText("Are you sure?").buildAlert().showAndWait();
        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                categoryNotesModel.getPassNoteData().removeAll(notesTableView.getSelectionModel().getSelectedItems());
            }
        });
    }

    @FXML
    private void noteEditButtonClick(ActionEvent event) {
        PassNoteFX editNote = notesTableView.getSelectionModel().getSelectedItem();
        if (editNote != null) {
            // setup stage and category data
            NoteStage noteStage = NoteStage.createForEdit(categoryNotesModel.getPassCategoryData(), editNote);
            NoteModel noteModel = noteStage.getModel();
            noteStage.showModal();

            if (noteModel.modalResult == ButtonType.OK) {
                categoryNotesModel.getPassNoteData().set(categoryNotesModel.getPassNoteData().indexOf(editNote), noteModel.getPassNoteFX());
            }
        }
    }

    @FXML
    private void noteDuplicateButtonClick(ActionEvent event) {
        // setup stage and category data
        NoteStage noteStage = NoteStage.createForDuplicate(
                categoryNotesModel.getPassCategoryData(),
                notesTableView.getSelectionModel().getSelectedItem());
        NoteModel noteModel = noteStage.getModel();
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

    @FunctionalInterface
    public interface FilePassConsumer {
        boolean accept(File f, String message);
    }

    private void processFile(File f, String initMasterPass, FilePassConsumer filePass, String processingMessage, String errorHeaderMessage, String errorContentMessage) {

        final String masterPass = initMasterPass == null ? MasterPassStage.queryMasterPass(f, null) : initMasterPass;

        if (masterPass != null) {
            showProgressNode(processingMessage);

            Task<Boolean> loadTask = new Task<Boolean>() {
                @Override
                protected Boolean call() throws InterruptedException  {
                    return filePass.accept(f, masterPass);
                }
            };

            loadTask.setOnSucceeded(e -> {
                hideProgressNode();
                if (loadTask.getValue()) {
                    categoryNotesModel.updateFile(f.getPath(), masterPass);
                    loadTreeView();
                }
                else
                    (new AlertDialogs.ErrorAlertBuilder()).setHeaderText(errorHeaderMessage + f.getPath()).setTitle("Error").setContentText(errorContentMessage).buildAlert().showAndWait();
            });

            executorService.execute(loadTask);
        }
    }

    private void loadVNF(File f) {
        processFile(f, null, categoryNotesModel::loadFile, "Loading ...", "Error reading file: ", "Wrong password or invalid file");
    }

    private void saveVNF(File f, String masterPass) {
        processFile(f, masterPass, categoryNotesModel::saveFile, "Saving ...", "Error saving file: ", "See logs for details");
    }

    private void importPINS(File f) {
        if (categoryNotesModel.importPINSFile(f)) {
            loadTreeView();
        }
    }

    /**
     * Loads file with given type
     * @param f File
     * @param fileType FileType
     */
    public void loadFile(File f, Document.FileType fileType) {
        switch (fileType) {
            case FT_VNF:
                loadVNF(f);
                break;
            case FT_IMPORT:
                importPINS(f);
        }
    }

    private void exportPINS(File f) {
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

            if (masterPass != null)
                saveVNF(f, masterPass);
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
            saveVNF(f, null);
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

    @FXML
    private void categoryMoveUpButtonClick(ActionEvent event) {
        TreeItem<PassCategoryFX> selectedItem = categoryTreeView.getSelectionModel().getSelectedItem();
        int selectedIndex = categoryTreeView.getSelectionModel().getSelectedIndex();

        if ((selectedItem != null) && (selectedIndex > 0)) {
            categoryNotesModel.categoryMoveUp(selectedItem.getValue());
            loadTreeView();
            categoryTreeView.getSelectionModel().select(selectedIndex - 1);
            categoryTreeView.requestFocus();
        }
    }

    @FXML
    private void categoryMoveDownButtonClick(ActionEvent event) {
        TreeItem<PassCategoryFX> selectedItem = categoryTreeView.getSelectionModel().getSelectedItem();
        int selectedIndex = categoryTreeView.getSelectionModel().getSelectedIndex();

        if ((selectedItem != null) && (selectedIndex < categoryTreeView.getRoot().getChildren().size() - 1)) {
            categoryNotesModel.categoryMoveDown(selectedItem.getValue());
            loadTreeView();
            categoryTreeView.getSelectionModel().select(selectedIndex + 1);
            categoryTreeView.requestFocus();
        }
    }

    @FXML
    private void noteMoveUpButtonClick(ActionEvent event) {
        TreeItem<PassCategoryFX> selectedCategory = categoryTreeView.getSelectionModel().getSelectedItem();
        PassNoteFX selectedNote = notesTableView.getSelectionModel().getSelectedItem();
        if ((selectedCategory != null) && (selectedNote != null)) {
            int selectedIndex = notesTableView.getSelectionModel().getSelectedIndex();
            if (selectedIndex > 0) {
                categoryNotesModel.noteMoveUp(selectedNote);
                loadTable(selectedCategory.getValue());
                notesTableView.getSelectionModel().clearSelection();
                notesTableView.getSelectionModel().select(selectedIndex - 1);
                notesTableView.requestFocus();
            }
        }
    }

    @FXML
    private void noteMoveDownButtonClick(ActionEvent event) {
        TreeItem<PassCategoryFX> selectedCategory = categoryTreeView.getSelectionModel().getSelectedItem();
        PassNoteFX selectedNote = notesTableView.getSelectionModel().getSelectedItem();
        if ((selectedCategory != null) && (selectedNote != null)) {
            int selectedIndex = notesTableView.getSelectionModel().getSelectedIndex();
            if (selectedIndex < notesTableView.getItems().size() - 1) {
                categoryNotesModel.noteMoveDown(selectedNote);
                loadTable(selectedCategory.getValue());
                notesTableView.getSelectionModel().clearSelection();
                notesTableView.getSelectionModel().select(selectedIndex + 1);
                notesTableView.requestFocus();
            }
        }
    }
}