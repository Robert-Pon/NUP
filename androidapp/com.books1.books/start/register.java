package com.books1.books.start;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.books1.connections.start.start;
import com.example.books.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class register extends Fragment {
    register_actions register_actions;
    String reservationID = "-";
    com.books1.connections.start.start start = new start();
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    TextView school;
    school_bottom_sheet school_bottom_sheet = new school_bottom_sheet();
    List<String> school_list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.register, container, false);
        ImageButton back = v.findViewById(R.id.back);
        Button register = v.findViewById(R.id.register);
        school = v.findViewById(R.id.school);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("&idR="+reservationID);
                        start.delete_reservation(stringBuilder.toString());
                    }
                };
                thread.start();
                register_actions.back();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("login="+((EditText)v.findViewById(R.id.login)).getText());
                stringBuilder.append("&password="+((EditText)v.findViewById(R.id.password)).getText());
                stringBuilder.append("&name="+((EditText)v.findViewById(R.id.name)).getText());
                stringBuilder.append("&surname="+((EditText)v.findViewById(R.id.surname)).getText());
                stringBuilder.append("&email="+((EditText)v.findViewById(R.id.email)).getText());
                stringBuilder.append("&city="+((EditText)v.findViewById(R.id.city)).getText());
                stringBuilder.append("&idR="+reservationID);
                stringBuilder.append("&school="+((TextView)v.findViewById(R.id.school)).getText().toString());


                StringBuilder school_data = new StringBuilder();
                school_data.append("login="+((EditText)v.findViewById(R.id.login)).getText());
                school_data.append("&password="+((EditText)v.findViewById(R.id.password)).getText());
                school_data.append("&name="+school_list.get(2).toLowerCase());
                school_data.append("&number="+school_list.get(0).toLowerCase());
                school_data.append("&type="+school_list.get(1).toLowerCase());
                school_data.append("&original="+((TextView)v.findViewById(R.id.school)).getText().toString());
                Random random = new Random();
                int r1 = random.nextInt(18302);
                Date date = new Date();
                school_data.append("&mID="+String.valueOf(r1)+"="+date.toString());
                Thread thread = new Thread(){
                    @Override
                    public void run() {


                        JSONObject message = start.register(stringBuilder.toString(),
                                school_data,
                                ((TextView)v.findViewById(R.id.school)).getText().toString()+" "+((EditText)v.findViewById(R.id.city)).getText().toString(),
                                ((EditText)v.findViewById(R.id.login)).getText().toString(),
                                ((EditText)v.findViewById(R.id.password)).getText().toString(),
                                getContext());
                        try {
                            register_actions.register(message.getString("id"), ((EditText)v.findViewById(R.id.login)).getText().toString(), ((EditText)v.findViewById(R.id.password)).getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
                register_actions.back();
            }
        });

        ((EditText)v.findViewById(R.id.login)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Context context = getContext();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        String data = "login=" + ((EditText)v.findViewById(R.id.login)).getText() + "&id=" + reservationID;
                        try {
                            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            editor = sharedPreferences.edit();
                            JSONObject message = start.login_checking(data);
                            reservationID = message.getString("id");
                            if (message.getString("type").toString().equals("100")) {
                                ((EditText)v.findViewById(R.id.login)).setTextColor(getResources().getColor(R.color.red));
                            } else if (message.getString("type").toString().equals("101")) {
                                reservationID = message.getString("id");
                                editor.putString("reservationID", reservationID);
                                editor.commit();
                                ((EditText)v.findViewById(R.id.login)).setTextColor(getResources().getColor(R.color.text));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                };
                thread.start();
            }
        });
        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                school_bottom_sheet.show(getChildFragmentManager(), "1");
            }
        });
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        register_actions = (register_actions) activity;
    }

    public interface register_actions{
        void register(String id, String login, String password);
        void back();
    }

    public void setSchool(String name, String number, String type){
        school_list.add(number);
        school_list.add(type);
        school_list.add(name);
        if(!number.equals("") && !number.equals("0")) {
            school.setText(number+ " " + type+ " " + name);
        }else{
            school.setText(type + " " + name);
        }
    }

}
