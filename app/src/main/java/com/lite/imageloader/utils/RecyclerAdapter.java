package com.lite.imageloader.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.lite.imageloader.R;
import java.util.ArrayList;

/**
 * Created by Saket on 02,August,2019
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ImageHolder> {

    private Context mContext;
    private ArrayList<Bitmap> bitmaps;

    public RecyclerAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(ArrayList<Bitmap> bitmaps){
        this.bitmaps = bitmaps;
    }
    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View relativeLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ImageHolder imageHolder = new ImageHolder(relativeLayout);
        return imageHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        holder.imView.setImageDrawable(mContext.getDrawable(R.drawable.ic_launcher_background));
        if(bitmaps!=null && bitmaps.size() > position) {
               holder.imView.setImageBitmap(bitmaps.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return 10;
    }

    public class ImageHolder extends RecyclerView.ViewHolder{
        public ImageView imView;
        public ImageHolder(View imageView){
            super(imageView);
            this.imView = (ImageView)imageView.findViewById(R.id.imView);
        }
    }
}
