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
import com.romanpulov.violetnotefx.Model.Document;
import com.romanpulov.violetnotefx.Model.PassCategoryFX;
import com.romanpulov.violetnotefx.Model.PassNoteFX;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 4540 on 22.02.2016.
 */
public class CategoryNotesModel {

    private ObservableList<PassNoteFX> passNoteData;
    private ObservableList<PassCategoryFX> passCategoryData;

    private BooleanProperty invalidatedData = new SimpleBooleanProperty(false);

    public BooleanProperty getInvalidatedData() {
        return invalidatedData;
    }

    private InvalidationListener modelInvalidationListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            invalidatedData.setValue(true);
        }
    };

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
        private PassData passData;

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
                    newPassNoteData.add(new PassNoteFX(passCategoryFX, passNote.getSystem(), passNote.getUser(), passNote.getPassword(), passNote.getComments(), passNote.getCustom(), passNote.getInfo()));
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
            passCategoryData.stream().forEach((p) -> {
                addCategoryData(categoryData, p);
            });
            categoryData.entrySet().stream().forEach((p) -> {
                passCategoryList.add(p.getValue());
            });

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
        try (OutputStream output = AESCryptService.generateCryptOutputStream(new FileOutputStream(f), masterPass)) {
            PassData passData = writePassData();

            (new XMLPassDataWriter(passData)).writeStream(output);

            Document.getInstance().setFile(f.getPath(), masterPass);
            invalidatedData.setValue(false);
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
}
