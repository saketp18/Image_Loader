package com.lite.imageloader.loader;

import android.app.Activity;
import androidx.annotation.NonNull;

/**
 * Created by Saket on 02,August,2019
 */
public class ImageLoaderApp{


    private static ImageLoader _imageLoad;

    public ImageLoaderApp() {
    }

    public ImageLoaderApp get(Activity context){
        _imageLoad = new ImageLoader(context);
        return this;
    }
    public ImageLoaderApp load(@NonNull String url){
        _imageLoad.loads(url);
        return this;
    }

    public ImageLoaderApp addMemCache(){
        _imageLoad.addMemCaches();
        return this;
    }

    public ImageLoaderApp addDiskCache(){

        return this;
    }

    public ImageLoaderApp build(){
        ImageLoaderApp im = new ImageLoaderApp();
        return im;
    }

    public void clearCache(){
        _imageLoad.clearMemCache();
    }
}
