package com.books1.books.main.fragments.profile.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.books.R;

public class settings_fragment_manager extends Fragment
{
    settings settings = new settings();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_fragment_manger, container, false);
        getChildFragmentManager().beginTransaction().replace(R.id.frame, settings).commit();
        return v;
    }




}
