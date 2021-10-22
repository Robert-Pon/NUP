package com.books1.books.main.fragments.new_book;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.books.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class show_img extends BottomSheetDialogFragment{
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.show_photo, container, false);
            Bundle bundle = this.getArguments();
            ImageView photo = v.findViewById(R.id.photo);
            photo.setImageURI(Uri.parse(bundle.getString("uri","-")));
            return v;
        }


}
