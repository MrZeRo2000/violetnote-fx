package com.romanpulov.violetnotefx;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by romanpulov on 06.04.2016.
 */
public class FileHelperTest {

    private static final String TEST_FILE_NAME = "data\\test.txt";
    private static final String TEST_NEW_FILE_NAME = "data\\test_new.txt";

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

    @Test
    public void fileNamesTest() {
        String fileName = TEST_FILE_NAME;
        System.out.println("FileName: " + fileName);
        String tempFileName = FileHelper.getTempFileName(fileName);
        System.out.println("Temp FileName: " + tempFileName);
        String revertFileName = FileHelper.removeExtension(tempFileName, FileHelper.FILE_TEMP_EXTENSION);
        System.out.println("Revert FileName: " + revertFileName);
        assertEquals(fileName, revertFileName);
    }

    @Test
    public void renameFileNameTest() throws Exception{
        // temp file name
        String tempFileName = FileHelper.getTempFileName(TEST_NEW_FILE_NAME);

        // delete if temp file exists
        File f = new File(tempFileName);
        if (f.exists())
            f.delete();



        // create test temp file
        FileOutputStream s = new FileOutputStream(tempFileName);
        s.write("XXX".getBytes());
        s.flush();
        s.close();

        //check if temp file exists
        f = new File(tempFileName);
        assertTrue(f.exists());

        // rename temp file
        FileHelper.renameTempFile(tempFileName);

        //check temp file does not exist
        f = new File(tempFileName);
        assertFalse(f.exists());

        //check if working file exists
        f = new File(TEST_NEW_FILE_NAME);
        assertTrue(f.exists());
    }

}
