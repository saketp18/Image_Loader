package com.lite.imageloader.loader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Saket on 02,August,2019
 */
public class CacheRequest {

    private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private final int cacheSize = maxMemory / 8;
    private LruCache<String, Bitmap> memoryCache;
    private boolean isCache = false;

    protected void init() {
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null && isCache) {
            memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }

    public void addBitmap(boolean isCache){
        this.isCache = isCache;
    }

    protected LruCache<String, Bitmap> getMemoryCache(){
        return memoryCache;
    }
}
