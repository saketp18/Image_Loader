package com.lite.imageloader.loader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by Saket on 02,August,2019
 */
public class ImageLoader implements NetworkInstance.NetworkResponse {

    private NetworkInstance _netwoNetworkInstance;
    private CacheRequest _caheCacheRequest;
    private ExecutorService mExecutorService;
    private BitmapListener _bitmBitmapListener;

    public ImageLoader() {
    }

    public ImageLoader(Activity context){
        _bitmBitmapListener = (BitmapListener)context;
        _netwoNetworkInstance = new NetworkInstance(this);
        _caheCacheRequest = new CacheRequest();
        mExecutorService = Executors.newFixedThreadPool(5);
        init();
    }

    protected void init(){
        _caheCacheRequest.init();
    }

    protected void loads(String url){
        if(_caheCacheRequest.getBitmapFromMemCache(url)==null)
           _netwoNetworkInstance.load(url);
    }

    protected void addMemCaches(){
        _caheCacheRequest.addMemCache();
    }

    protected void clearMemCache(){
        _caheCacheRequest.clearCache();
    }
    @Override
    public void onSuccess(InputStream inputStream, String url) {

        Bitmap bmp = BitmapFactory.decodeStream(inputStream);
        if(_caheCacheRequest.getIsCache())
            _caheCacheRequest.addBitmapToMemoryCache(url, bmp);
        _bitmBitmapListener.onBitmapReady(bmp);
    }



    @Override
    public void onFailure() {
        Log.d("SaketResponse", "Failed!!");
        _bitmBitmapListener.onBitmapFailure();
    }

    //Use this scale method when you are needed to downscale a large image.
    /*protected static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }*/

    public interface BitmapListener{
        public void onBitmapReady(Bitmap bitmap);
        public void onBitmapFailure();
    }
}
