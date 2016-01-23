package com.romanpulov.violetnotefx.masterpass;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rpulov on 23.01.2016.
 */
public class MasterPassPresenter {
    private static final Logger log = LoggerFactory.getLogger(MasterPassPresenter.class);

    @FXML
    private Button okButton;

    @FXML
    private void okButtonClick() {
        log.debug("Ok button clicked");
    }
}
