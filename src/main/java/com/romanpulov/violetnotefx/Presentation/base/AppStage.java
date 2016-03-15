package com.romanpulov.violetnotefx.Presentation.base;

import com.romanpulov.violetnotefx.Core.stages.AbstractStage;
import com.romanpulov.violetnotefx.Core.stages.AbstractTypedStage;
import com.romanpulov.violetnotefx.MainApp;

/**
 * Created by rpulov on 14.03.2016.
 */
public abstract class AppStage<D, M, C> extends AbstractTypedStage<D, M, C> {

    @Override
    protected void afterCreateStage() {
        MainApp.setupStageIcons(stage);
    }
}
