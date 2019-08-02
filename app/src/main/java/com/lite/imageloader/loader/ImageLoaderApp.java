package com.lite.imageloader.loader;

import android.app.Activity;

/**
 * Created by Saket on 02,August,2019
 */
public class ImageLoaderApp {


    private static ImageLoader _imageLoad;

    public ImageLoaderApp get(Activity context){
        _imageLoad = new ImageLoader(context);
        return this;
    }
    public ImageLoaderApp load(String url){
        _imageLoad.load(url);
        return this;
    }

    public ImageLoaderApp addMemCache(boolean isCached){
        _imageLoad.addMemCache(isCached);
        return this;
    }

    public ImageLoaderApp addDiskCache(boolean isCache){

        return this;
    }

    public ImageLoaderApp build(){
        ImageLoaderApp im = new ImageLoaderApp();
        return im;
    }
}
