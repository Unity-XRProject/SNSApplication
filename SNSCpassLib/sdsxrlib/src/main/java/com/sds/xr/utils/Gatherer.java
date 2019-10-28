package com.sds.xr.utils;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

public class Gatherer {
    public static String DCIM_ROOT = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
    public static String DOWNLOAD_ROOT = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
    public static void loadAllImagesPaths(String rootFolder, ArrayList<String> allImages) {
        File file = new File(rootFolder);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        loadAllImagesPaths(f.getAbsolutePath(),allImages);
                    } else {
                        if (f.getAbsolutePath().endsWith(".png")) {
                            allImages.add(f.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }
}
