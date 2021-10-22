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

public class buy_bottom extends BottomSheetDialogFragment {
    String id, login, password = "";

    TextView price;
    Button cancel;
    Button buy;

    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.buy, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sharedPreferences.getString("id","");
        login = sharedPreferences.getString("login","");
        password = sharedPreferences.getString("password","");

        price = v.findViewById(R.id.price);
        cancel = v.findViewById(R.id.cancel);
        buy = v.findViewById(R.id.buy);

        if (id.equals("-")){
            dismiss();
            Toast.makeText(getContext(), "Zaloguj sie aby kupić", Toast.LENGTH_LONG).show();
        }

        Bundle bundle = this.getArguments();

        price.setText("Cena to : " + bundle.getString("price", "") + " "+ bundle.getString("currency", ""));

        buy.setOnClickListener(new View.OnClickListener() {
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
                        String message = shop_c.buy(data.toString());
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
