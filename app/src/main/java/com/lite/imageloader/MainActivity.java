package com.lite.imageloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.lite.imageloader.loader.ImageLoader;
import com.lite.imageloader.loader.ImageLoaderApp;
import com.lite.imageloader.unplashloader.Photo;
import com.lite.imageloader.unplashloader.PhotoList;
import com.lite.imageloader.unplashloader.UnplashClient;
import com.lite.imageloader.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ImageLoader.RequestResponse {
    @NonNull
    private ImageView imview = null;
    @NonNull
    private ImageLoader imageLoader;
    @NonNull
    private ImageView imView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imview = (ImageView)findViewById(R.id.imView);
        imView2 = (ImageView)findViewById(R.id.imView2);
        //ImageLoaderApp im  = new ImageLoaderApp().get(this).load(Constants.URL).build();
        loadPhots();
    }

    public void loadPhots(){
        PhotoList list = UnplashClient.getUnplashClient().create(PhotoList.class);
        Call<List<Photo>> call = list.getPhotos(1, 5);
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if(response.isSuccessful()){
                    for(Photo photo: response.body()) {
                        Log.d("Saket", photo.getUrls().getRegular());
                        Log.d("Saket", photo.getUrls().getSmall());
                        Log.d("Saket", photo.getUrls().getFull());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Please check Internet!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void setBitmap2(Bitmap bmp) {
        imView2.setImageBitmap(bmp);
    }

    @Override
    public void setBitmap(Bitmap bmp) {
        imview.setImageBitmap(bmp);

    }
}
