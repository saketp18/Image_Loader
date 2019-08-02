package com.lite.imageloader.loader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import com.lite.imageloader.utils.Constants;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by Saket on 02,August,2019
 */
public class ImageLoader implements NetworkInstance.NetworkResponse {

    private NetworkInstance _netwoNetworkInstance;
    private RequestResponse _resRequestResponse;
    private CacheRequest _caheCacheRequest;
    private ExecutorService mExecutorService;
    public ImageLoader(Activity context){
        _netwoNetworkInstance = new NetworkInstance(this);
        _resRequestResponse =  (RequestResponse)context;
        _caheCacheRequest = new CacheRequest();
        mExecutorService = Executors.newFixedThreadPool(10);
        init();
    }

    public void init(){
        _caheCacheRequest.init();
        _netwoNetworkInstance.load(Constants.URL, 1);
    }

    public void load(String url){
        mExecutorService.submit(new NetworkInstance().new LoadImages(url));
        _netwoNetworkInstance.load(Constants.URL, 1);
    }

    public void addMemCache(boolean isCached){
        _caheCacheRequest.addBitmap(isCached);
    }
    @Override
    public void onSuccess(InputStream inputStream, String keys, int key) {

        Bitmap bmp = null;
        bmp = BitmapFactory.decodeStream(inputStream);
        bmp = scaleBitmap(bmp, bmp.getWidth() / 4, bmp.getHeight() / 4);
        _caheCacheRequest.addBitmapToMemoryCache(Constants.URL, bmp);
        _resRequestResponse.setBitmap(bmp);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }
    @Override
    public void onFailure() {
        Log.d("SaketResponse", "Failed!!");
    }

    public interface RequestResponse{
        public void setBitmap(Bitmap bmp);
    }
}
