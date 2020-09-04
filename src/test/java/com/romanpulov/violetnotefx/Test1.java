package com.romanpulov.violetnotefx;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.romanpulov.violetnotefx.presentation.masterpass.MasterPassView;

public class Test1 {

    @Test
    public void testMethod() {
        assertEquals(1, 1);
    }

    @Test
    public void conventionalView() {
        class TestView extends MasterPassView {

        }

        TestView tv = new TestView();
        assertEquals(tv.getConventionalResourceName(), "/fxml/test.fxml");
    }
}
