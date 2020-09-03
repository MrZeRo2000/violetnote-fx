package com.romanpulov.violetnotefx.presentation;

import com.romanpulov.violetnotefx.core.dialogs.AlertDialogs;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Created by romanpulov on 05.04.2016.
 */
public class DialogsHelper {
    public static boolean queryUnsavedData() {
        Optional<ButtonType> result = new AlertDialogs.ConfirmationAlertBuilder()
                .setContentText("You have unsaved data. Are you sure?")
                .setDefaultButton(ButtonType.CANCEL)
                .buildAlert()
                .showAndWait();
        return result.isPresent() && result.get().equals(ButtonType.OK);
    }
}
