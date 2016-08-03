package com.romanpulov.violetnotefx.Presentation.categorynotes;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.PinsDataReader;
import com.romanpulov.violetnotecore.Processor.PinsDataWriter;
import com.romanpulov.violetnotecore.Processor.XMLPassDataReader;
import com.romanpulov.violetnotecore.Processor.XMLPassDataWriter;
import com.romanpulov.violetnotefx.FileHelper;
import com.romanpulov.violetnotefx.Model.Document;
import com.romanpulov.violetnotefx.Model.PassCategoryFX;
import com.romanpulov.violetnotefx.Model.PassNoteFX;
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

    public final class PassDataReader {
        private final PassData passData;

        public PassDataReader(PassData passData) {
            this.passData = passData;
        }

        private PassCategoryFX findSourcePassCategory(PassCategory passCategory) {
            for (PassCategoryFX p : passCategoryData) {
                if (p.getSourcePassCategory().equals(passCategory))
                    return p;
            }
            return null;
        }

        private ObservableList<PassCategoryFX> readCategoryData() {
            ObservableList<PassCategoryFX> newPassCategoryData = FXCollections.observableArrayList();
            passData.getPassCategoryList().stream().forEach((passCategory -> {
                PassCategoryFX newPassCategoryFX = addPassCategoryFX(passCategory);
                if (newPassCategoryFX != null){
                    newPassCategoryData.add(newPassCategoryFX);
                }
            }));

           return newPassCategoryData;
        }

        private ObservableList<PassNoteFX> readNoteData() {
            ObservableList<PassNoteFX> newPassNoteData =  FXCollections.observableArrayList();
            passData.getPassNoteList().stream().forEach((passNote)-> {
                PassCategoryFX passCategoryFX = findSourcePassCategory(passNote.getPassCategory());
                if (passCategoryFX != null)
                    newPassNoteData.add(new PassNoteFX(passCategoryFX,
                            passNote.getSystem(),
                            passNote.getUser(),
                            passNote.getPassword(),
                            passNote.getComments(),
                            passNote.getCustom(),
                            passNote.getInfo()));
            });

            return newPassNoteData;
        }
    }

    public final class PassDataWriter {
        public PassData writePassData() {
            PassData data = new PassData();
            List<PassCategory> passCategoryList = new ArrayList<>();
            List<PassNote> passNoteList = new ArrayList<>();
            data.setPassCategoryList(passCategoryList);
            data.setPassNoteList(passNoteList);

            Map<PassCategoryFX, PassCategory> categoryData = new HashMap<>();
            passCategoryData.stream().forEach((p) -> addCategoryData(categoryData, p));
            categoryData.entrySet().stream().forEach((p) -> passCategoryList.add(p.getValue()));

            passNoteData.stream().forEach((p) -> {
                PassCategory category = categoryData.get(p.getCategory());
                PassNote note = new PassNote(category, p.getSystem(), p.getUser(), p.getRealPassword(), p.getComments(), p.getComments(), p.getInfo());
                passNoteList.add(note);
            });

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

    private PassCategory addCategoryData(Map<PassCategoryFX, PassCategory> categoryData, PassCategoryFX categoryFX) {
        PassCategory category = categoryData.get(categoryFX);
        if (category == null) {
            // create new systemTextField_textProperty if not exists
            category = new PassCategory(categoryFX.getCategoryName());
            // create and set parent systemTextField_textProperty
            PassCategoryFX parentCategoryFX = categoryFX.getParentCategory();
            if (parentCategoryFX != null) {
                PassCategory parentPassCategory = addCategoryData(categoryData, parentCategoryFX);
                category.setParentCategory(parentPassCategory);
            }
            categoryData.put(categoryFX, category);
        }
        return category;
    }

    public void readPassData(PassData passData) {
        PassDataReader pdr = new PassDataReader(passData);
        setPassCategoryData(pdr.readCategoryData());
        setPassNoteData(pdr.readNoteData());
    }

    public PassData writePassData() {
        return new PassDataWriter().writePassData();
    }

    public ObservableList<PassCategoryFX> getCategoryData() {
        return passCategoryData;
    }

    public ObservableList<PassNoteFX> getPassNoteData(PassCategoryFX category) {
        return new FilteredList<>(passNoteData, p -> p.getCategory().equals(category));
    }

    public boolean loadFile(File f, String masterPass) {
        if (f.exists()) {
            try (InputStream input = AESCryptService.generateCryptInputStream(new FileInputStream(f), masterPass)) {
                PassData passData = (new XMLPassDataReader()).readStream(input);
                readPassData(passData);

                Document.getInstance().setFile(f.getPath(), masterPass);
                invalidatedData.setValue(false);
                return true;
            } catch (AESCryptException | IOException | DataReadWriteException e) {
                e.printStackTrace();
                return false;
            }
        } else
            return false;
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
        if (!result)
            return false;

        Document.getInstance().setFile(f.getPath(), masterPass);
        invalidatedData.setValue(false);

        return true;
    }

    private boolean saveFileInternal(File f, String masterPass) {
        try (OutputStream output = AESCryptService.generateCryptOutputStream(new FileOutputStream(f), masterPass)) {
            PassData passData = writePassData();

            (new XMLPassDataWriter(passData)).writeStream(output);

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
            readPassData(passData);
            invalidatedData.setValue(true);
            return true;
        } catch (DataReadWriteException | FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean exportPINSFile(File f) {
        try (OutputStream output = new FileOutputStream(f)) {
            PassData passData = writePassData();
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

    public static <T> void listMoveDown(T item, List<? super T> list) {
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
}
