package com.books1.books.main.fragments.auctions;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.books.R;
import com.books1.books.urls;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class show_img extends BottomSheetDialogFragment{
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.show_photo, container, false);
            Bundle bundle = this.getArguments();
            ImageView photo = v.findViewById(R.id.photo);
            Thread  get_photo = new Thread(){
                @Override
                public void run() {
                    try {


                                urls urls = new urls();
                                URL url = new URL(urls.ip() + urls.photos_dir() + bundle.getString("photo"));
                                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                InputStream inputStream = httpURLConnection.getInputStream();
                                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                                Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Matrix matrix = new Matrix();
                                matrix.postRotate(90);
                                Bitmap rotate = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                                photo.setImageBitmap(rotate);

                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            get_photo.start();
            return v;
        }


}
