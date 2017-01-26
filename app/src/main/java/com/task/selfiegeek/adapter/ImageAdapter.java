package com.task.selfiegeek.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Cache;
import com.squareup.picasso.Picasso;
import com.task.selfiegeek.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLConnection;
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
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 64;
        //Bitmap bmp = BitmapFactory.decodeFile(imageData.get(position),options);
        /*if(isImageFile(imageData.get(position)))
        { int THUMBSIZE = 128;

            Bitmap bmp = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile( imageData.get(position)),
                    THUMBSIZE,
                    THUMBSIZE);
        holder.image.setImageBitmap(bmp);}*/
        if (isImageFile(imageData.get(position))) {
            Picasso.with(context)

                    .load(new File(imageData.get(position))).resize(500, 500).into(holder.image);
        } else {
            //Bitmap bmp = ThumbnailUtils.createVideoThumbnail(imageData.get(position),MediaStore.Images.Thumbnails.MICRO_KIND);
            //BitmapFactory.Options mo = new BitmapFactory().O
            //holder.image.setImageBitmap(bmp);
            Glide.with(context).load(imageData.get(position)).into(holder.image);

        }
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
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if (isImageFile(imageData.get(getAdapterPosition())))
                        intent.setDataAndType(Uri.fromFile(new File(imageData.get(getAdapterPosition()))), "image/.jpg");
                    else
                        intent.setDataAndType(Uri.fromFile(new File(imageData.get(getAdapterPosition()))), "video/");
                    context.startActivity(intent);
                }
            });
        }
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

}
