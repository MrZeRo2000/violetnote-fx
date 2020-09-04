package com.romanpulov.violetnotefx.presentation.categorynotes;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.Model.*;
import com.romanpulov.violetnotecore.Processor.*;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotefx.FileHelper;
import com.romanpulov.violetnotefx.model.Document;
import com.romanpulov.violetnotefx.model.PassCategoryFX;
import com.romanpulov.violetnotefx.model.PassNoteFX;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 4540 on 22.02.2016.
 */
public class CategoryNotesModel {

    private ObservableList<PassNoteFX> passNoteData;
    private ObservableList<PassCategoryFX> passCategoryData;

    public static class PassNoteSearch {

        final List<PassNoteFX> passNoteData;
        private static PassNoteSearch searchInstance;

        private List<PassNoteFX> searchPassNoteData;
        private int searchPosition;
        private final String searchString;
        private final String processSearchString;

        public PassNoteSearch(List<PassNoteFX> passNoteData, String searchString) {
            this.passNoteData = passNoteData;
            this.searchString = searchString;
            this.processSearchString = searchString.toLowerCase();
            this.searchPosition = -1;
        }

        private PassNoteFX peekSearchResult() {
            if ((searchPassNoteData != null) && (searchPassNoteData.size() > 0))
                if (searchPosition < searchPassNoteData.size() - 1)
                    return searchPassNoteData.get(searchPosition ++);
                else
                    return searchPassNoteData.get(searchPosition);
            else
                return null;
        }

        private void calcSearchResult() {
            searchPassNoteData = passNoteData.stream().filter(p -> {
                String s = searchString.toLowerCase();
                return
                        p.getSystem().toLowerCase().contains(s) ||
                        p.getUser().toLowerCase().contains(s);
            }).collect(Collectors.toList());
            if (searchPassNoteData.size() > 0)
                searchPosition = 0;
            else
                searchPosition = - 1;
        }

        public static PassNoteFX requestSearch(List<PassNoteFX> passNoteData, String searchString) {
            if ((searchInstance == null) || (!searchInstance.searchString.equals(searchString))) {
                searchInstance = new PassNoteSearch(passNoteData, searchString);
                searchInstance.calcSearchResult();
            }
            return searchInstance.peekSearchResult();
        }

        public static void clearSearch() {
            searchInstance = null;
        }
    }

    public PassNoteFX requestSearch(String searchString) {
        return PassNoteSearch.requestSearch(passNoteData, searchString);
    }

    public void clearSearch() {
        PassNoteSearch.clearSearch();
    }

    private final BooleanProperty invalidatedData = new SimpleBooleanProperty(false);

    public BooleanProperty getInvalidatedData() {
        return invalidatedData;
    }

    /*
    private final InvalidationListener modelInvalidationListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            invalidatedData.setValue(true);
        }
    };
    */

    private final InvalidationListener modelInvalidationListener = (p) -> invalidatedData.setValue(true);

    {
        initData();
    }

    private String loadFileName;
    private Document.FileType loadFileType = Document.FileType.FT_NONE;

    public void updateFileType() {
        int dotPos = loadFileName.lastIndexOf(".");
        if (dotPos > 0) {
            String extension = loadFileName.substring(dotPos + 1).toLowerCase();
            loadFileType = Document.FILE_TYPES.get(extension);
        } else
            loadFileType = Document.FileType.FT_NONE;
    }

    public void setLoadFileName(String loadFileName) {
        this.loadFileName = loadFileName;
        updateFileType();
    }

    public String getLoadFileName() {
        return loadFileName;
    }

    public Document.FileType getLoadFileType() {
        return loadFileType;
    }

    private String lastUpdateCategoryName;

    public String getLastUpdateCategoryName() {
        return lastUpdateCategoryName;
    }

    public final static class PassDataReader {
        private final PassData2 passData2;

        private ObservableList<PassCategoryFX> passCategoryData;

        public ObservableList<PassCategoryFX> getPassCategoryData() {
            return passCategoryData;
        }

        private ObservableList<PassNoteFX> passNoteData;

        public ObservableList<PassNoteFX> getPassNoteData() {
            return passNoteData;
        }

        public PassDataReader(PassData2 passData) {
            this.passData2 = passData;
        }

        public void readPassData() {
            passCategoryData = FXCollections.observableArrayList();
            passNoteData =  FXCollections.observableArrayList();

            passData2.getCategoryList().forEach(passCategory2 -> {
                PassCategoryFX passCategoryFX = new PassCategoryFX(null, passCategory2.getCategoryName());
                passCategoryData.add(passCategoryFX);

                passCategory2.getNoteList().forEach(passNote2 -> {
                    passNoteData.add(new PassNoteFX(
                            passCategoryFX,
                            passNote2.getSystem(),
                            passNote2.getUser(),
                            passNote2.getPassword(),
                            passNote2.getUrl(),
                            passNote2.getInfo()
                    ));
                });
            });

        }

    }

    public final class PassDataWriter {
         private final ObservableList<PassNoteFX> passNoteData;

        public PassDataWriter(ObservableList<PassNoteFX> passNoteData) {
            this.passNoteData = passNoteData;
        }

        public PassData2 writePassData() {
            PassData2 data = new PassData2();
            List<PassCategory2> passCategoryList = new ArrayList<>();
            Map<PassCategoryFX, PassCategory2> categoryData = new HashMap<>();

            passNoteData.forEach(passNoteFX -> {
                // find category and add if not found
                PassCategory2 passCategory2 = categoryData.get(passNoteFX.getCategory());
                if (passCategory2 == null) {
                    passCategory2 = new PassCategory2(passNoteFX.getCategoryName());
                    passCategory2.setNoteList(new ArrayList<>());
                    categoryData.put(passNoteFX.getCategory(), passCategory2);
                    passCategoryList.add(passCategory2);
                }

                // add note
                passCategory2.getNoteList().add(new PassNote2(
                        passNoteFX.getSystem(),
                        passNoteFX.getUser(),
                        passNoteFX.getPassword(),
                        passNoteFX.getUrl(),
                        passNoteFX.getInfo(),
                        null,
                        null
                ));
            });

            data.setCategoryList(passCategoryList);

            return data;
        }
    }

    public ObservableList<PassCategoryFX> getPassCategoryData() {
        return passCategoryData;
    }

    public void initData() {
        setPassCategoryData(FXCollections.observableArrayList());
        setPassNoteData(FXCollections.observableArrayList());
        invalidatedData.setValue(false);
    }

    public void setPassCategoryData(ObservableList<PassCategoryFX> passCategoryData) {
        if (this.passCategoryData != null) {
            this.passCategoryData.removeListener(modelInvalidationListener);
        }
        this.passCategoryData = passCategoryData;
        this.passCategoryData.addListener(modelInvalidationListener);
    }

    public void setPassNoteData(ObservableList<PassNoteFX> passNoteData) {
        if (this.passNoteData != null) {
            this.passNoteData.removeListener(modelInvalidationListener);
        }
        this.passNoteData = passNoteData;
        this.passNoteData.addListener(modelInvalidationListener);
    }

    public ObservableList<PassNoteFX> getPassNoteData() {
        return passNoteData;
    }

    public PassCategoryFX findChildPassCategoryName(PassCategoryFX parentPassCategory, String categoryName) {
        for (PassCategoryFX p : passCategoryData) {
            if ((p.getParentCategory() == parentPassCategory) && (p.getCategoryName().equals(categoryName))) {
                return p;
            }
        }
        return null;
    }

    /*
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
    */

    private PassCategory2 addCategoryData(Map<PassCategoryFX, PassCategory2> categoryData, PassCategoryFX categoryFX) {
        PassCategory2 category = categoryData.get(categoryFX);
        if (category == null) {
            // create new systemTextField_textProperty if not exists
            category = new PassCategory2(categoryFX.getCategoryName());
            categoryData.put(categoryFX, category);
        }
        return category;
    }

    public void readPassData(PassData2 passData2) {
        PassDataReader pdr = new PassDataReader(passData2);
        pdr.readPassData();

        setPassCategoryData(pdr.getPassCategoryData());
        setPassNoteData(pdr.getPassNoteData());
    }

    public PassData2 writePassData() {
        return new PassDataWriter(passNoteData).writePassData();
    }

    public ObservableList<PassCategoryFX> getCategoryData() {
        return passCategoryData;
    }

    public ObservableList<PassNoteFX> getPassNoteData(PassCategoryFX category) {
        return new FilteredList<>(passNoteData, p -> p.getCategory().equals(category));
    }

    public boolean loadFile(File f, String masterPass) {
        if (f.exists()) {
            try (InputStream inputStream =new FileInputStream(f)) {

                FilePassDataReaderV2 reader = new FilePassDataReaderV2(inputStream, masterPass);
                PassData2 passData2 = reader.readFile();

                readPassData(passData2);

                return true;
            } catch (AESCryptException | IOException | DataReadWriteException e) {
                e.printStackTrace();
                return false;
            }
        } else
            return false;
    }

    public void updateFile(String fileName, String masterPass) {
        Document.getInstance().setFile(fileName, masterPass);
        invalidatedData.setValue(false);
    }

    public boolean saveFile(File f, String masterPass) {
        boolean result;

        // save as temp file first
        File tempFile = new File(FileHelper.getTempFileName(f.getPath()));
        result = saveFileInternal(tempFile, masterPass);
        if (!result)
            return false;

        //roll backup files
        result = FileHelper.saveCopies(f.getPath());
        if (!result)
            return false;

        //rename temp file
        result = FileHelper.renameTempFile(tempFile.getPath());
        return result;
    }

    private boolean saveFileInternal(File f, String masterPass) {
        try (OutputStream outputStream = new FileOutputStream(f)) {
            PassData2 passData2 = writePassData();

            FilePassDataWriterV2 writer = new FilePassDataWriterV2(outputStream, masterPass, passData2);
            writer.writeFile();

            return true;
        } catch (AESCryptException | IOException | DataReadWriteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean importPINSFile(File f) {
        PassData passData;
        PinsDataReader pinsReader = new PinsDataReader();
        try {
            passData = pinsReader.readStream(new FileInputStream(f));
            // readPassData(passData);
            invalidatedData.setValue(true);
            return true;
        } catch (DataReadWriteException | FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean exportPINSFile(File f) {
        try (OutputStream output = new FileOutputStream(f)) {
            // PassData passData = writePassData();
            PassData passData = new PassData();
            (new PinsDataWriter()).writeStream(output, passData);
            output.flush();
            output.close();
            return true;
        } catch(IOException | DataReadWriteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static <T> void listMoveUp(T item, List<? super T> list) {
        int index = list.indexOf(item);
        if (index > 0) {
            list.remove(index);
            list.add(index - 1, item);
        }
    }

    private static <T> void listMoveDown(T item, List<? super T> list) {
        int index = list.indexOf(item);
        if (index < list.size() - 1) {
            list.remove(index);
            list.add(index + 1, item);
        }
    }

    public void categoryMoveUp(PassCategoryFX item) {
        listMoveUp(item, passCategoryData);
    }

    public void categoryMoveDown(PassCategoryFX item) {
        listMoveDown(item, passCategoryData);
    }

    public void noteMoveUp(PassNoteFX note) {
        listMoveUp(note, passNoteData);
    }

    public void noteMoveDown(PassNoteFX note) {
        listMoveDown(note, passNoteData);
    }

    public void noteSwap(PassNoteFX note1, PassNoteFX note2) {
        Collections.swap(passNoteData, passNoteData.indexOf(note1), passNoteData.indexOf(note2));
    }

    public void notesUpdateCategory(List<PassNoteFX> noteList, PassCategoryFX categoryFX) {
        noteList.forEach((noteFX) ->  noteFX.getCategoryProperty().setValue(categoryFX));
        lastUpdateCategoryName = categoryFX.getCategoryName();
    }
}
