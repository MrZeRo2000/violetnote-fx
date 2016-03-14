package com.romanpulov.violetnotefx.Core.stages;

import com.romanpulov.violetnotefx.Core.injection.Injector;
import com.romanpulov.violetnotefx.Core.views.FXMLAbstractView;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by rpulov on 14.03.2016.
 */
public abstract class AbstractStage {
    private static final String STAGE_CLASS_NAME_ENDING = "Stage";
    private static final String MODEL_CLASS_NAME_ENDING = "Model";
    private static final String PRESENTER_CLASS_NAME_ENDING = "Presenter";
    private static final String VIEW_CLASS_NAME_ENDING = "View";

    private String getConventionalCore() {
        String conventionalCore = this.getClass().getName();
        int endingIndex = conventionalCore.lastIndexOf(STAGE_CLASS_NAME_ENDING);
        if (endingIndex > 0) {
            conventionalCore = conventionalCore.substring(0, endingIndex);
        }
        return conventionalCore;
    }

    protected Object model;
    protected Object controller;
    protected FXMLAbstractView view;
    protected Stage stage;
    protected Scene scene;

    protected abstract Modality getModality();

    protected void beforeCreateStage() {}
    protected void afterCreateStage() {};
    protected void afterShowScene() {}

    public AbstractStage(Object data) {

    }

    public void show() {
        beforeCreateStage();

        String conventionalCore = getConventionalCore();

        view = (FXMLAbstractView)Injector.instantiateClass(conventionalCore + VIEW_CLASS_NAME_ENDING);
        stage = new Stage();
        scene = new Scene(view.getView());
        stage.setScene(scene);
        model = view.getModelInstance();
        controller = view.getControllerInstance();

        afterCreateStage();

        Modality modality = getModality();
        if (modality.equals(Modality.NONE)) {
            stage.show();
        } else {
            stage.setResizable(false);
            stage.showAndWait();
        }

        afterShowScene();
    }
}
