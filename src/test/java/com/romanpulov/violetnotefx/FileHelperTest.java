package com.romanpulov.violetnotefx;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by romanpulov on 06.04.2016.
 */
public class FileHelperTest {

    private static final String TEST_FILE_NAME = "data\\test.txt";

    @Test
    public void testMethod() {
        assertEquals(1, 1);
    }

    public void saveCopiesTest() throws Exception {
        File f = new File(TEST_FILE_NAME);
        assertTrue(f.exists());

        FileHelper.saveCopies(TEST_FILE_NAME);

        FileOutputStream s = new FileOutputStream(TEST_FILE_NAME);
        s.write("2".getBytes());
        s.flush();
        s.close();
    }

}
