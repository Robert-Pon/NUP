package com.books1.books.main.fragments.profile.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.books.R;

public class settings extends Fragment {
    Button out;
    TextView profile;
    TextView my_shop;
    TextView notifications;
    TextView password;
    settings_in settings_in;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    com.books1.books.main.fragments.profile.settings.password_changer password_changer = new password_changer();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();

        profile = v.findViewById(R.id.profile);
        notifications = v.findViewById(R.id.notifications);
        my_shop = v.findViewById(R.id.my_shop);
        password = v.findViewById(R.id.password_changer);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings_in.profile_settings();
            }
        });
        out = v.findViewById(R.id.out);
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("in", false);
                editor.commit();
                settings_in.log_out();
            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings_in.notifications();
            }
        });

        my_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings_in.my_shop();
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_changer = new password_changer();
                password_changer.show(getChildFragmentManager(), "D");
            }
        });
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        settings_in = (settings_in) context;
    }

    public interface settings_in{
        public void profile_settings();
        public void notifications();
        public void my_shop();
        public void log_out();
    }



}
