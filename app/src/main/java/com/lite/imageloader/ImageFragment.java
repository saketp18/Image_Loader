package com.lite.imageloader;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lite.imageloader.utils.BitmapResponse;

/**
 * Created by Saket on 03,August,2019
 */
public class ImageFragment extends Fragment {

    private Bitmap bmp;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int sync = getArguments().getInt("bitmap", -1);
        bmp = BitmapResponse.get().getLargeData(sync);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view, parent, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = (ImageView)view.findViewById(R.id.iamge);
        imageView.setImageBitmap(bmp);
    }
}
