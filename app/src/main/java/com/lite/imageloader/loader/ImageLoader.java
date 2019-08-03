package com.lite.imageloader.loader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.lite.imageloader.utils.Constants;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;


/**
 * Created by Saket on 02,August,2019
 */
public class ImageLoader implements NetworkInstance.NetworkResponse {

    private NetworkInstance _netwoNetworkInstance;
    private CacheRequest _caheCacheRequest;
    private DiskLruCacheRequest _diskLruCacheRequest;
    private ExecutorService mExecutorService;
    private BitmapListener _bitmBitmapListener;

    public ImageLoader(Activity context) {
        _bitmBitmapListener = (BitmapListener) context;
        _netwoNetworkInstance = new NetworkInstance(this);
        _caheCacheRequest = new CacheRequest();
        _diskLruCacheRequest = new DiskLruCacheRequest(context, "image_loader", Constants.DEFAULT_DISK_CACHE_SIZE, Bitmap.CompressFormat.JPEG, 70);
        mExecutorService = Executors.newFixedThreadPool(5);
        init();
    }

    protected void init() {
        _caheCacheRequest.init();
    }

    protected void loads(String url, String key) {
        if (_caheCacheRequest.getBitmapFromMemCache(key) == null && !_diskLruCacheRequest.containsKey(key)) {
            _netwoNetworkInstance.load(url, key);
        } else if (_caheCacheRequest.getBitmapFromMemCache(key) != null) {
            onSuccess(_caheCacheRequest.getBitmapFromMemCache(key));
        } else {
            onSuccess(_diskLruCacheRequest.getBitmap(key));
        }
    }

    protected void addMemCaches() {
        _caheCacheRequest.addMemCache();
    }

    protected void clearMemCache() {
        _caheCacheRequest.clearCache();
    }

    protected void addDiskCaches() {
        _diskLruCacheRequest.addDiskCache();
    }

    protected void clearDiskCache() {
        _diskLruCacheRequest.clearCache();
    }

    protected void cancel() {
        _netwoNetworkInstance.cancel();
    }

    @Override
    public void onSuccess(InputStream inputStream, String url, String key) {

        Bitmap bmp = BitmapFactory.decodeStream(inputStream);
        if (_caheCacheRequest.getIsCache()) {
            _caheCacheRequest.addBitmapToMemoryCache(key, bmp);
        }
        if (_diskLruCacheRequest.isCache()) {
            _diskLruCacheRequest.addBitmapDiskCache(key, bmp);
        }
        _bitmBitmapListener.onBitmapReady(bmp);
    }

    public void onSuccess(Bitmap bmp) {
        _bitmBitmapListener.onBitmapReady(bmp);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("SaketResponse", "Failed!!");
        _bitmBitmapListener.onBitmapFailure(call, t);
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

    public interface BitmapListener {
        void onBitmapReady(Bitmap bitmap);

        void onBitmapFailure(Call<ResponseBody> call, Throwable t);
    }
}
