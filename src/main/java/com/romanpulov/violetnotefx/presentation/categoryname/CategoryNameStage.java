package com.romanpulov.violetnotefx.Presentation.categoryname;

import com.romanpulov.violetnotefx.Presentation.base.AppStage;

/**
 * Created by rpulov on 14.03.2016.
 */
public class CategoryNameStage extends AppStage<CategoryNameModel, CategoryNamePresenter> {
    @Override
    protected void afterCreateStage() {
        super.afterCreateStage();
        stage.setTitle("Category Name");
    }
}
