package com.romanpulov.violetnotefx.Presentation.categorynotes;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.PinsDataReader;
import com.romanpulov.violetnotecore.Processor.XMLPassDataReader;
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
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
                // find existing
                PassCategoryFX passCategoryFX = findSourcePassCategory(passCategory);
                // if not found then add
                if (passCategoryFX == null) {
                    PassCategoryFX newPassCategoryFX = addPassCategoryFX(passCategory);
                    if (newPassCategoryFX != null){
                        newPassCategoryData.add(newPassCategoryFX);
                    }
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
                PassNote note = new PassNote(category, p.getSystem(), p.getUser(), p.getPassword(), p.getComments(), p.getComments(), p.getInfo());
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

    private void readDocument() {
        if (Document.getInstance().getPassData() != null) {
            readPassData(Document.getInstance().getPassData());
        }
    }

    private void writeDocument() {
        Document.getInstance().setPassData(writePassData());
    }

    public void readPassData(PassData passData) {
        PassDataReader pdr = new PassDataReader(passData);
        setPassCategoryData(pdr.readCategoryData());
        setPassNoteData(pdr.readNoteData());
    }

    public PassData writePassData() {
        return new PassDataWriter().writePassData();
    }

    public CategoryNotesModel() {
        readDocument();
    }

    public ObservableList<PassCategoryFX> getCategoryData() {
        return passCategoryData;
    }

    public ObservableList<PassNoteFX> getPassNoteData(PassCategoryFX category) {
        return new FilteredList<PassNoteFX>(passNoteData, p -> p.getCategory().equals(category));
    }

    public boolean loadFile(File f) {
        return true;
    }

    public boolean saveFile(File f) {
        return true;
    }

    public boolean importPINSFile(File f) {
        PassData passData;
        PinsDataReader pinsReader = new PinsDataReader();
        passData = null;
        try {
            passData = pinsReader.readStream(new FileInputStream(f));
            readPassData(passData);
            return true;
        } catch (DataReadWriteException | FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
