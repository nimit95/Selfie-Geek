package com.task.selfiegeek.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.task.selfiegeek.R;

import java.util.List;

/**
 * Created by Nimit Agg on 22-01-2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<String> imageData;
    private Context context;

    public ImageAdapter(Context context, List<String> imageData) {
        this.context = context;
        this.imageData = imageData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_list_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*Glide.with(context)
                .load(imageData.get(position).getPreviewURL())
                .into(holder.image);*/
        Bitmap bmp = BitmapFactory.decodeFile(imageData.get(position));
        holder.image.setImageBitmap(bmp);
    }

    @Override
    public int getItemCount() {
        return imageData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.preview_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  //   Log.e("jn",imageData.get(getAdapterPosition()).getId() + "");
                    Intent intent = new Intent(context, ImageDetail.class)
                            .putExtra("image_detail", imageData.get(getAdapterPosition())).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);*/
                }
            });
        }
    }
}
