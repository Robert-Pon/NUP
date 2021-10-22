package com.books1.books.start;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.books1.connections.start.start;
import com.example.books.R;

public class account_back extends Fragment {
    Button send;
    EditText email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_back, container, false);

        send = v.findViewById(R.id.send);
        email = v.findViewById(R.id.email);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread back = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        start start = new start();
                        String data = "email="+email.getText();
                        start.account_back(data);
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Wysłano !", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                };
                back.start();
            }
        });
        return v;
    }
}
