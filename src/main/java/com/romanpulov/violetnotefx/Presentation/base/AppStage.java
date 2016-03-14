package com.romanpulov.violetnotefx.Presentation.base;

import com.romanpulov.violetnotefx.Core.stages.AbstractStage;
import com.romanpulov.violetnotefx.MainApp;

/**
 * Created by rpulov on 14.03.2016.
 */
public abstract class AppStage extends AbstractStage {

    public AppStage(Object data) {
        super(data);
    }

    @Override
    protected void afterCreateStage() {
        MainApp.setupStageIcons(stage);
    }
}
