package com.lite.imageloader;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lite.imageloader.loader.ImageLoader;
import com.lite.imageloader.loader.ImageLoaderApp;
import com.lite.imageloader.models.Photo;
import com.lite.imageloader.unplashloader.PhotoList;
import com.lite.imageloader.unplashloader.UnplashClient;
import com.lite.imageloader.utils.RecyclerAdapter;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ImageLoader.BitmapListener{
    private ImageLoaderApp imageLoaderApp;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private MainActivity _instance;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private Button mCancel;
    private Call<List<Photo>> call;

    private View.OnClickListener mClicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.cancel) {
                call.cancel();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _instance = this;
        setContentView(R.layout.activity_main);
        mAdapter = new RecyclerAdapter(this);
        mRecyclerView = findViewById(R.id.recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mCancel = findViewById(R.id.cancel);
        mCancel.setOnClickListener(mClicklistener);
        loadPhotos();
    }

    public void loadPhotos() {
        PhotoList list = UnplashClient.getUnplashClient().create(PhotoList.class);
        call = list.getPhotos(2, 100);
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()) {
                    for (Photo photo : response.body()) {
                        imageLoaderApp = new ImageLoaderApp().get(_instance)
                                .load(photo.getUrls().getRegular(), photo.getId()).addMemCache().addDiskCache()
                                .build();

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (call.isCanceled()) {
                    Toast.makeText(getApplicationContext(), "Loading Canceled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(Bitmap bitmap: bitmaps){
            if(!bitmap.isRecycled()){
                bitmap.recycle();
            }
        }
    }

    @Override
    public void onBitmapReady(Bitmap bitmap) {
        bitmaps.add(bitmap);
        mAdapter.setData(bitmaps);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBitmapFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("Saket", "Bitmap decoding failed");
        if(t instanceof SocketTimeoutException){
            Toast.makeText(getApplicationContext(), "Time out, Please check internet!", Toast.LENGTH_SHORT).show();
        }
    }
}
