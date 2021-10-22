package com.books1.books.main.fragments.profile;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.books.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class bottom_sheet_uploading extends BottomSheetDialogFragment {
    TextView uploading;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.uploading_fragment, container, false);
        uploading = v.findViewById(R.id.uploading);
        uploading.setText("Przygotowywanie do wysłania");
        return v;
    }
    public void setText(String text){
        ((Activity) getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                uploading.setText(text);
            }
        });
    }

}
