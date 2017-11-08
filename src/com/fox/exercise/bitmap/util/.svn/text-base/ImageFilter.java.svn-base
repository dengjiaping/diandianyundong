package com.fox.exercise.bitmap.util;

import java.io.File;
import java.io.FileFilter;

public class ImageFilter
        implements FileFilter {
    private static final String[] IMAGE;

    static {
        String[] arrayOfString = new String[5];
        arrayOfString[0] = ".png";
        arrayOfString[1] = ".jpg";
        arrayOfString[2] = ".jpeg";
        arrayOfString[3] = ".gif";
        arrayOfString[4] = ".bmp";
        IMAGE = arrayOfString;
    }

    public boolean accept(File paramFile) {
        int i;
        if ((paramFile.isHidden()) || (paramFile.isDirectory())) {
            i = 0;
            return i > 0 ? true : false;
        }
        String[] arrayOfString = null;
        int j = 0;
        if (paramFile.isFile() && paramFile.length() > 0) {
            arrayOfString = IMAGE;
            j = arrayOfString.length;
        }
        for (int k = 0; ; ++k) {
            if (k >= j) {
                i = 0;
                return i > 0 ? true : false;
            }


            String str = arrayOfString[k];
            if (!paramFile.getName().toLowerCase().endsWith(str))
                continue;
            i = 1;
            break;
        }
        return i > 0 ? true : false;
    }
}