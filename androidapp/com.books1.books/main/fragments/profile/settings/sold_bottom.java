package com.books1.books.main.fragments.profile.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.books.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class sold_bottom extends BottomSheetDialogFragment {
    TextView nups;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sold_bottom, container, false);
        Bundle bundle = this.getArguments();

        nups = v.findViewById(R.id.nups);

        nups.setText("Gratuluje, zdobyłeś "+bundle.getString("nups")+" nupsów !");
        return v;
    }
}
