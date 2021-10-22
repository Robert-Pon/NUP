package com.books1.books.start;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.books.R;

public class start extends Fragment{
    start_actions start_actions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start, container, false);
        Button login = v.findViewById(R.id.login);
        Button register = v.findViewById(R.id.register);
        Button account_back = v.findViewById(R.id.account_back);
        Button with_out = v.findViewById(R.id.with_out);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_actions.go_to_login();
            }
        });

         register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_actions.go_to_register();
            }
        });

         account_back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 start_actions.go_to_account_back();
             }
         });

         with_out.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                 SharedPreferences.Editor editor = sharedPreferences.edit();
                 editor.putString("id","-");
                 editor.putString("login","-");
                 editor.putString("password","-");
                 editor.commit();
                 start_actions.go_to_guest_view();

             }
         });
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        start_actions = (start_actions) activity;
    }

    public interface start_actions{
        void go_to_login();
        void go_to_register();
        void go_to_account_back();
        void go_to_guest_view();
    }
}
