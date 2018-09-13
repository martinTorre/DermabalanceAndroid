package com.dermabalance.utils;

import android.os.Environment;

import java.io.File;

public class FileUtils {
    public static void createDirectory(final String directoryName) {
        try {
            final File directory = new File(Environment.getExternalStorageDirectory()
                    + File.separator + directoryName);
            directory.mkdirs();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
