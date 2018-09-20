package com.romanpulov.violetnotefx.presentation.base;

import com.romanpulov.violetnotefx.core.presentation.AbstractStage;
import javafx.scene.image.Image;

/**
 * Created by rpulov on 14.03.2016.
 */
public abstract class AppStage<M, C> extends AbstractStage<M, C> {
    @Override
    protected void afterCreateStage() {
        stage.getIcons().addAll(
                new Image("images/Icon_16_16.png"),
                new Image("images/Icon_32_32.png")
        );
    }
}
