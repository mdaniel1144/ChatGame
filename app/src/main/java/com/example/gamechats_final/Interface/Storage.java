package com.example.gamechats_final.Interface;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import java.io.ByteArrayOutputStream;

public class Storage {

    private static FirebaseStorage m_Storage = FirebaseStorage.getInstance();
    private final int SELECT_PICTURE = 200;
    public static Task<Void> UploadImage(String i_Location , String i_ImageSrc  , ImageView image)
    {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        String path = i_Location+"/"+ i_ImageSrc;
        StorageReference mountainImagesRef = m_Storage.getReference().child(path);
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG , "Failed Upload Image Profile: "+i_ImageSrc);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG , "Success Upload Image Profile: "+ i_ImageSrc);
            }
        });

        return taskCompletionSource.getTask();
    }

    public static void GetImageFromStorage(String i_Location,String i_ImageSrc , ImageView i_Image) {

        StorageReference storageRef = m_Storage.getReference();
        String path = i_Location+"/" + i_ImageSrc;
        StorageReference islandRef = storageRef.child(path);

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                i_Image.setImageBitmap(bmp);
            }
        });
    }
}
