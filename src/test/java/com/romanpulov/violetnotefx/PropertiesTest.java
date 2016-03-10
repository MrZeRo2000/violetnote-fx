package com.romanpulov.violetnotefx;

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
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
    public void testProperties() throws Exception {
        Properties p = PropertiesManager.getInstance().getProperties();
        p.put("Prop 1", "Value1");
        PropertiesManager.getInstance().save();
        p.clear();
        assertNotEquals(p.getProperty("Prop 1"), "Value1");
        PropertiesManager.getInstance().load();
        assertEquals(p.getProperty("Prop 1"), "Value1");
        assertEquals(p.size(), 1);
        PropertiesManager.getInstance().load();
        assertEquals(p.size(), 1);
        PropertiesManager.getInstance().deletePropertiesFile();
    }
}
