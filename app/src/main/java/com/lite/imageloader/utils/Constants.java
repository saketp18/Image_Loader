package com.lite.imageloader.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Saket on 02,August,2019
 */
public class Constants {

    public static final String SECTET_KEY ="3faf3b709961d159252c470641c0cedd042f81f78e77cfb7bbb15402941cffc5";
    public static final String ACCESS_KEY = "568b663e8d6cbe1778fcdd7ade5f65640ccd435c52777d29b0cb09c2734137f4";
    public static final String AUTH_CODE = "9fea061e7881df59866d704f71ee49cdad2ca356eee675c97a09d32e050e5398";
    public static final String BASE = "https://api.unsplash.com/";
    public static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 50; // 100MB
    public static final int IO_BUFFER_SIZE = 8 * 1024;

    public static boolean isExternalStorageRemovable() {
        return Environment.isExternalStorageRemovable();
    }

    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    public static boolean hasExternalCacheDir() {
        return true;
    }

}
