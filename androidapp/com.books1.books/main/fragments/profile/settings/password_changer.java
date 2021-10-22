package com.books1.books.main.fragments.profile.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.books1.connections.main.profile_c;
import com.example.books.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class password_changer extends BottomSheetDialogFragment {
    EditText password1;
    EditText password2;

    Button accept;

    String id, login, password = "";

    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.password_changer, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sharedPreferences.getString("id","");
        login = sharedPreferences.getString("login","");
        password = sharedPreferences.getString("password","");

        password1 = v.findViewById(R.id.password);
        password2 = v.findViewById(R.id.password2);
        accept = v.findViewById(R.id.accept);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        if(password1.getText().toString().equals(password2.getText().toString())) {
                            StringBuilder data = new StringBuilder();
                            data.append("login=" + login);
                            data.append("&password=" + password);
                            data.append("&newPassword=" + password1.getText().toString());
                            data.append("&id=" + id);
                            profile_c profile_c = new profile_c();
                            profile_c.password_changer(data.toString());
                            dismiss();
                        }else{
                            try {
                                ((Activity) getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "Hasła nie są takie same !", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }catch (Exception e){

                            }


                        }
                    }
                };
                thread.start();
            }
        });

        return v;
    }
}
