package com.books1.books.main.fragments.auctions;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.books.R;
import com.books1.connections.main.shop_c;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class exchange_bottom extends BottomSheetDialogFragment {
    String id, login, password = "";

    TextView exchange;
    Button cancel;
    Button exchange_b;

    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exchange_bottom, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sharedPreferences.getString("id","");
        login = sharedPreferences.getString("login","");
        password = sharedPreferences.getString("password","");

        exchange = v.findViewById(R.id.exchange);
        cancel = v.findViewById(R.id.cancel);
        exchange_b = v.findViewById(R.id.exchange_b);

        Bundle bundle = this.getArguments();
        if(bundle.getString("exchange", "").trim().equals("")){
            dismiss();
        }
        if(id.equals("-")){
            Toast.makeText(getContext(), "Zaloguj sie aby wymienić książke !", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        exchange.setText("Wymień się za: " + bundle.getString("exchange", ""));

        exchange_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        StringBuilder data = new StringBuilder();
                        data.append("id="+id);
                        data.append("&login="+login);
                        data.append("&password="+password);
                        data.append("&id1="+bundle.getString("book_id"));
                        shop_c shop_c = new shop_c();
                        String message = shop_c.exchange(data.toString());
                        //Toast.makeText(getContext(), "Zakupiono książkę ! Skontaktuj się z sprzedającym i ustal jak ją odbierzesz !", Toast.LENGTH_LONG).show();
                    }
                };
                thread.start();
                dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }
}
