package com.romanpulov.violetnotefx.Core.presentation;

import com.romanpulov.violetnotefx.Core.injection.Injector;
import com.romanpulov.violetnotefx.Core.views.FXMLAbstractView;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by 4540 on 15.03.2016.
 */
public abstract  class AbstractStage<M, C> {
    private static final String STAGE_CLASS_NAME_ENDING = "Stage";
    private static final String VIEW_CLASS_NAME_ENDING = "View";

    private String getConventionalCore() {
        String conventionalCore = this.getClass().getName();
        int endingIndex = conventionalCore.lastIndexOf(STAGE_CLASS_NAME_ENDING);
        if (endingIndex > 0) {
            conventionalCore = conventionalCore.substring(0, endingIndex);
        }
        return conventionalCore;
    }

    protected M model;
    protected C controller;
    protected FXMLAbstractView view;
    protected Stage stage;
    protected Scene scene;

    protected void beforeCreateStage() {}
    protected void afterCreateStage() {};
    protected void afterShowStage() {}

    @SuppressWarnings("unchecked")
    public M createStage() {
        beforeCreateStage();

        String conventionalCore = getConventionalCore();

        view = (FXMLAbstractView) Injector.instantiateClass(conventionalCore + VIEW_CLASS_NAME_ENDING);
        stage = new Stage();
        scene = new Scene(view.getView());
        stage.setScene(scene);

        model = (M)view.getModelInstance();
        controller = (C)view.getControllerInstance();

        afterCreateStage();
        return model;
    }

    public void show() {
        showStage(Modality.NONE);
    }

    public void showModal() {
        showStage(Modality.APPLICATION_MODAL);
    }

    public void showStage(Modality modality) {
        stage.initModality(modality);

        if (modality.equals(Modality.NONE)) {
            stage.show();
        } else {
            stage.setResizable(false);
            stage.showAndWait();
        }

        afterShowStage();
    }
}
