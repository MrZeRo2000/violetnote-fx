package com.romanpulov.violetnotefx;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by romanpulov on 06.04.2016.
 */
public class FileHelper {
    private static int FILE_KEEP_COPIES_COUNT = 5;
    private static String FILE_COPY_FORMAT = "%s.bak%02d";

    public static boolean saveCopies(String fileName) {
        try {
            for (int cp = FILE_KEEP_COPIES_COUNT - 1; cp >= 0; cp--) {
                File fc = new File(cp == 0 ? fileName : String.format(FILE_COPY_FORMAT, fileName, cp));
                String copyFileName = String.format(FILE_COPY_FORMAT, fileName, cp + 1);
                if (fc.exists()) {
                    Files.copy(fc.toPath(), FileSystems.getDefault().getPath(copyFileName), StandardCopyOption.REPLACE_EXISTING);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
