package com.lite.imageloader.utils;

import android.content.Context;
import android.os.Environment;
import java.io.File;

/**
 * Created by Saket on 02,August,2019
 */
public class Constants {

    public static final String ACCESS_KEY = "38088471cb7fa7bc33c9c17576c478971f2473fb11c5076f37dd7c78704cab2f";
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

        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    public static boolean hasExternalCacheDir() {
        return true;
    }
}
