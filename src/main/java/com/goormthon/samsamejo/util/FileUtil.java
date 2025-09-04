package com.goormthon.samsamejo.util;

import java.util.UUID;

public class FileUtil {

    public static String getRandomFileName(String fileName) {
        return UUID.randomUUID() + getFileExtensionWithDot(fileName);
    }

    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    public static String getFileExtensionWithDot(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }
}
