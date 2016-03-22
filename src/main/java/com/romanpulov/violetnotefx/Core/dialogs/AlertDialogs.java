package com.romanpulov.violetnotefx.Core.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

/**
 * Created by 4540 on 24.02.2016.
 */
public class AlertDialogs {

    public static abstract class AlertBuilder {
        protected Alert.AlertType alertType;
        protected String title;
        protected String headerText;
        protected String contentText;
        protected ButtonType defaultButton;

        protected abstract void setDefaults();

        public AlertBuilder(Alert.AlertType alertType) {
            this.alertType = alertType;
        }

        public AlertBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public AlertBuilder setHeaderText(String headerText) {
            this.headerText = headerText;
            return this;
        }

        public AlertBuilder setContentText(String contentText) {
            this.contentText = contentText;
            return this;
        }

        public AlertBuilder setDefaultButton(ButtonType defaultButton) {
            this.defaultButton = defaultButton;
            return this;
        }

        public Alert buildAlert() {
            Alert alert = new Alert(this.alertType);
            setDefaults();
            alert.setTitle(this.title);
            alert.setHeaderText(this.headerText);
            alert.setContentText(this.contentText);

            if (defaultButton != null)
                for (ButtonType buttonType : alert.getButtonTypes())
                    ((Button)alert.getDialogPane().lookupButton(buttonType)).setDefaultButton(buttonType == defaultButton);

            return alert;
        }
    }

    public static class InformationAlertBuilder extends AlertBuilder {

        public InformationAlertBuilder() {
            super(Alert.AlertType.INFORMATION);
        }

        @Override
        protected void setDefaults() {
            this.title = "Information";
        }
    }

    public static class ErrorAlertBuilder extends AlertBuilder {

        public ErrorAlertBuilder() {
            super(Alert.AlertType.ERROR);
        }

        @Override
        protected void setDefaults() {
            this.title = "Error";
        }
    }

    public static class WarningAlertBuilder extends AlertBuilder {

        public WarningAlertBuilder() {
            super(Alert.AlertType.WARNING);
        }

        @Override
        protected void setDefaults() {
            this.title = "Warning";
        }
    }

    public static class ConfirmationAlertBuilder extends AlertBuilder {

        public ConfirmationAlertBuilder() {
            super(Alert.AlertType.CONFIRMATION);
        }

        @Override
        protected void setDefaults() {
            this.title = "Confirmation";
        }
    }
}
