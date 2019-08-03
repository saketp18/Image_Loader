package com.lite.imageloader.loader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Saket on 02,August,2019
 */
public class CacheRequest {

    private static LruCache<String, Bitmap> memoryCache; // static will stick the images for certain period of time.
    private static boolean isCache = false;
    private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private final int cacheSize = maxMemory;

    protected void init() {
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    protected void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    protected Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }

    protected boolean getIsCache() {
        return isCache;
    }

    protected void addMemCache() {
        isCache = true;
    }

    protected void clearCache() {
        memoryCache.evictAll();
    }

    protected LruCache<String, Bitmap> getMemoryCache() {
        return memoryCache;
    }
}
