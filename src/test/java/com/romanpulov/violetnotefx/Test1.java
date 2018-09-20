package com.romanpulov.violetnotefx;

import static org.junit.Assert.*;

import com.romanpulov.violetnotefx.presentation.masterpass.MasterPassView;
import org.junit.Test;

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