package com.books1.books.start;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.books1.connections.start.start;
import com.example.books.R;

import org.json.JSONException;
import org.json.JSONObject;

public class login extends Fragment {
    login_actions login_actions;
    com.books1.connections.start.start start = new start();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login, container, false );
        v.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_actions.back();
            }
        });

        v.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                boolean in = false;
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("login=" + ((EditText)v.findViewById(R.id.login_input)).getText());
                        stringBuilder.append("&password=" + ((EditText)v.findViewById(R.id.password)).getText());
                        JSONObject message = start.login(stringBuilder.toString());
                        ((Activity)getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    switch (message.getString("type")) {
                                        case "101":
                                            login_actions.login(message.getString("id"), ((EditText) v.findViewById(R.id.login_input)).getText().toString(), ((EditText)v.findViewById(R.id.password)).getText().toString());
                                            break;
                                        case "103":
                                            Toast.makeText(getContext(), "Użytkownik o takim loginie nie istnieje !", Toast.LENGTH_LONG).show();
                                            break;
                                        case "104":
                                            Toast.makeText(getContext(), "Nie poprawne hasło !", Toast.LENGTH_LONG).show();
                                            break;

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }

                };
                thread.start();
            }

        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        login_actions = (login_actions)activity;
    }

    public interface login_actions{
        void login(String id, String login, String password);
        void back();
        void error();
    }
}
