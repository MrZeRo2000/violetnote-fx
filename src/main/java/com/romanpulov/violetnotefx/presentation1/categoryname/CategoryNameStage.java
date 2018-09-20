package com.romanpulov.violetnotefx.Presentation.categoryname;

import com.romanpulov.violetnotefx.Model.PassCategoryFX;
import com.romanpulov.violetnotefx.Presentation.base.AppStage;

/**
 * Created by rpulov on 14.03.2016.
 */
public class CategoryNameStage extends AppStage<CategoryNameModel, CategoryNamePresenter> {

    private static final String TITLE_CATEGORY_ADD = "Category Add";
    private static final String TITLE_CATEGORY_VIEW = "Category View";
    private static final String TITLE_CATEGORY_EDIT = "Category Edit";


    private String title;

    private CategoryNameStage() {
    }

    private static CategoryNameStage createEditable(PassCategoryFX category, String title) {
        CategoryNameStage categoryNameStage = new CategoryNameStage();
        categoryNameStage.createStage();
        CategoryNameModel categoryNameModel = categoryNameStage.getModel();
        categoryNameModel.setPassCategoryFX(category);

        categoryNameStage.title = title;

        return categoryNameStage;
    }

    public static CategoryNameStage createReadOnly(PassCategoryFX category) {
        CategoryNameStage categoryNameStage = new CategoryNameStage();
        categoryNameStage.createStage();
        CategoryNameModel categoryNameModel = categoryNameStage.getModel();
        categoryNameModel.setPassCategoryFXReadOnly(category);

        categoryNameStage.title = TITLE_CATEGORY_VIEW;

        return categoryNameStage;
    }


    public static CategoryNameStage createForAdd(PassCategoryFX parentCategory) {
        return createEditable(PassCategoryFX.newEmptyInstance(parentCategory), TITLE_CATEGORY_ADD);
    }

    public static CategoryNameStage createForEdit(PassCategoryFX parentCategory, PassCategoryFX category) {
        return createEditable(PassCategoryFX.newInstance(parentCategory, category), TITLE_CATEGORY_EDIT);
    }


    @Override
    protected void afterCreateStage() {
        super.afterCreateStage();
        stage.setTitle(title);
    }
}
