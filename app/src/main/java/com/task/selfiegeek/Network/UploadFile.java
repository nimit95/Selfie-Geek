package com.task.selfiegeek.Network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.core.UploaderProgressListener;
import com.kinvey.java.model.FileMetaData;
import com.task.selfiegeek.utils.Constants;

import java.io.File;
import java.io.IOException;

/**
 * Created by Nimit Agg on 22-01-2017.
 */

public class UploadFile {
    private Context context;
    private GetClient getClient;
    public UploadFile(Context context) {
        this.context = context;
        getClient = new GetClient(context);
    }
    public void uploadFile(final File target) throws IOException{
        FileMetaData meta = new FileMetaData(target.getName())     ;
        meta.setId(target.getName());
       getClient.getClient().file().upload(meta, target, new UploaderProgressListener() {
           @Override
           public void progressChanged(MediaHttpUploader mediaHttpUploader) throws IOException {
               Log.i(Constants.TAG, "upload progress: " + mediaHttpUploader.getUploadState());
           }

           @Override
           public void onSuccess(FileMetaData fileMetaData) {
                Toast.makeText(context,"Uploaded "+ target.getName(),Toast.LENGTH_SHORT ).show();
           }

           @Override
           public void onFailure(Throwable throwable) {
               Log.e(Constants.TAG, "failed to upload: " , throwable);
           }
       });

/*        new GetClient(context).getClient().file().upload(meta, target, new UploaderProgressListener() {

            @Override
            public void progressChanged(MediaHttpUploader mediaHttpUploader) throws IOException {

            }

            @Override
            public void onSuccess(FileMetaData fileMetaData) {

            }

            @Override
            public void onFailure(Throwable error) {
                Log.e(Constants.TAG, "failed to upload: " , error);
            }

        *//*    @Override
            public void progressChanged(MediaHttpUploader uploader) throws IOException {
                Log.i(Constants.TAG, "upload progress: " + uploader.getUploadState());

                final String state = new String(uploader.getUploadState().toString());

                context
            }
*//*
        });*/
    }
}
