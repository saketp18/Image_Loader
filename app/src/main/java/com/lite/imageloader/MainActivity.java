package com.lite.imageloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.lite.imageloader.loader.ImageLoader;
import com.lite.imageloader.loader.ImageLoaderApp;
import com.lite.imageloader.models.Photo;
import com.lite.imageloader.unplashloader.PhotoList;
import com.lite.imageloader.unplashloader.UnplashClient;
import com.lite.imageloader.utils.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ImageLoader.BitmapListener{
    private ImageLoaderApp imageLoaderApp;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private MainActivity _instance;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _instance = this;
        setContentView(R.layout.activity_main);
        mAdapter = new RecyclerAdapter(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        loadPhotos();
    }

    public void loadPhotos(){
        PhotoList list = UnplashClient.getUnplashClient().create(PhotoList.class);
        Call<List<Photo>> call = list.getPhotos(1, 10);
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if(response.isSuccessful()){
                    for(Photo photo: response.body()) {
                        imageLoaderApp = new ImageLoaderApp().get(_instance)
                                            .load(photo.getUrls().getRegular()).addMemCache().build();

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
    protected void onDestroy() {
        super.onDestroy();
        imageLoaderApp.clearCache();
    }

    @Override
    public void onBitmapReady(Bitmap bitmap) {
        bitmaps.add(bitmap);
        mAdapter.setData(bitmaps);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBitmapFailure() {
        Log.d("Saket","Bitmap decoding failed");
    }
}
