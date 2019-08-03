package com.lite.imageloader;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lite.imageloader.loader.ImageLoader;
import com.lite.imageloader.loader.ImageLoaderApp;
import com.lite.imageloader.models.Photo;
import com.lite.imageloader.unplashloader.PhotoList;
import com.lite.imageloader.unplashloader.UnplashClient;
import com.lite.imageloader.utils.BitmapResponse;
import com.lite.imageloader.utils.RecyclerAdapter;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ImageLoader.BitmapListener, RecyclerAdapter.ItemClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private MainActivity _instance;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private Button mCancel;
    private Call<List<Photo>> call;
    private ProgressBar mProgess;
    private ImageView errorView;

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
        mProgess = findViewById(R.id.progress);
        errorView = findViewById(R.id.error);
        loadPhotos();
    }

    public void loadPhotos() {
        PhotoList list = UnplashClient.getUnplashClient().create(PhotoList.class);
        call = list.getPhotos(2, 25);
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                mCancel.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    for (Photo photo : response.body()) {
                        new ImageLoaderApp().get(_instance)
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
                    Toast.makeText(getApplicationContext(), "Please check your internet!!", Toast.LENGTH_SHORT).show();
                }
                mProgess.setVisibility(View.GONE);
                errorView.setImageDrawable(getDrawable(android.R.drawable.stat_notify_error));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Bitmap bitmap : bitmaps) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        _instance = null;
    }

    @Override
    public void onBitmapReady(Bitmap bitmap) {
        mProgess.setVisibility(View.GONE);
        bitmaps.add(bitmap);
        mAdapter.setData(bitmaps);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBitmapFailure(Call<ResponseBody> call, Throwable t) {
        if (call.isCanceled()) {
            Toast.makeText(getApplicationContext(), "Loading Canceled", Toast.LENGTH_SHORT).show();
        }
        if (t instanceof SocketTimeoutException) {
            Toast.makeText(getApplicationContext(), "Time out, Please check internet!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickListener(int position) {
        Bitmap bmp = bitmaps.get(position);
        int sync = BitmapResponse.get().setLargeData(bmp);
        //So the idea was to pass a reference code that only ImageFragment would have.
        // That would ensure that it is retrieving the correct object reference from the singleton.
        // Otherwise, null will be returned which should be handled by fail-safe methods.
        Bundle bundle = new Bundle();
        bundle.putInt("bitmap", sync);
        ImageFragment imageFragment = new ImageFragment();
        imageFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, imageFragment).addToBackStack(null);
        transaction.commit();
    }
}
