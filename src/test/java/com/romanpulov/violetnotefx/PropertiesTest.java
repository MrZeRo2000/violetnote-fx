package com.romanpulov.violetnotefx;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by 4540 on 10.03.2016.
 */
public class PropertiesTest {

    @Test
    public void testMethod() {
        assertEquals(1, 1);
    }

    @Test
    public void testProperties() {
        final String propName = "Prop 1";
        final String propValue = "Value1";
        final String propValue2 = "Value2";

        PropertiesManager propertiesManager = PropertiesManager.getInstance();

        propertiesManager.setProperty(propName, propValue);
        assertTrue(propertiesManager.isModified());

        propertiesManager.save();

        propertiesManager.clear();

        assertNotEquals(propValue, propertiesManager.getProperty(propName));

        propertiesManager.load();
        assertEquals(propValue, propertiesManager.getProperty(propName));
        assertFalse(propertiesManager.isModified());

        propertiesManager.setProperty(propName, propValue2);
        assertTrue(propertiesManager.isModified());

        propertiesManager.deletePropertiesFile();
    }
}
