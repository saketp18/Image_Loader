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
    private ItemClickListener itemClickListener;

    public RecyclerAdapter(Context context) {
        this.mContext = context;
        itemClickListener = (ItemClickListener)context;
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
    public void onBindViewHolder(@NonNull ImageHolder holder, final int position) {
        holder.imView.setImageDrawable(mContext.getDrawable(R.drawable.ic_launcher_background));
        if(bitmaps!=null && bitmaps.size() > position) {
               holder.imView.setImageBitmap(bitmaps.get(position));
        }
        holder.imView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onClickListener(position);
            }
        });
    }


    //Our Item count is constant because not unware how many items to be loaded.
    @Override
    public int getItemCount() {
        if(bitmaps !=null)
            return bitmaps.size();
        else
            return 0;
    }

    public class ImageHolder extends RecyclerView.ViewHolder{
        public ImageView imView;
        public ImageHolder(View imageView){
            super(imageView);
            this.imView = (ImageView)imageView.findViewById(R.id.imView);
        }
    }

    public interface ItemClickListener{
        public void onClickListener(int position);
    }
}
