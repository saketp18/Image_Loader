package com.lite.imageloader.utils;

import android.graphics.Bitmap;

/**
 * Created by Saket on 03,August,2019
 */
public class BitmapResponse {

    private static BitmapResponse instance;
    private int counter = 0;
    private Bitmap bmp;

    public synchronized static BitmapResponse get() {
        if (instance == null) {
            instance = new BitmapResponse();
        }
        return instance;
    }

    public int setLargeData(Bitmap largeData) {
        this.bmp = largeData;
        return ++counter;
    }

    public Bitmap getLargeData(int request) {
        return (request == counter) ? bmp : null;
    }
}

