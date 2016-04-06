package com.romanpulov.violetnotefx;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Created by romanpulov on 06.04.2016.
 */
public class FileHelper {
    private static int FILE_KEEP_COPIES_COUNT = 5;
    private static final String FILE_COPY_FORMAT = "%s.bak%02d";
    public static final String FILE_TEMP_EXTENSION = ".temp";

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

    public static String getTempFileName(String fileName) {
        return fileName + FILE_TEMP_EXTENSION;
    }

    public static String removeExtension(String fileName, String extension) {
        return fileName.substring(0, fileName.lastIndexOf(extension));
    }

    public static String getRevertFileName(String fileName) {
        return removeExtension(fileName, FILE_TEMP_EXTENSION);
    }

    public static boolean renameTempFile(String tempFileName) {
        String targetFileName = getRevertFileName(tempFileName);
        Path tempFilePath = FileSystems.getDefault().getPath(tempFileName);
        Path targetFilePath = FileSystems.getDefault().getPath(targetFileName);
        try {
            Files.copy(tempFilePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
            Files.delete(tempFilePath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
