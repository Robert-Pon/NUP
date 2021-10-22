package com.books1.books.start;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.books1.books.main.main;
import com.example.books.R;

public class fragment_manager extends AppCompatActivity implements
        com.books1.books.start.login.login_actions,
        com.books1.books.start.register.register_actions,
        com.books1.books.start.start.start_actions,
school_bottom_sheet.school_in{

    login login = new login();
    register register = new register();
    start start = new start();
    account_back account_back = new account_back();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_manager);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, start).commit();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean("in", false)==true){
            go_to_main();
        }
    }

    @Override
    public void login(String id, String login, String password) {
        editor.putString("id", id);
        editor.putString("login", login);
        editor.putString("password", password);
        editor.putBoolean("in", true);
        editor.commit();
        go_to_main();
    }

    @Override
    public void register(String id, String login, String password) {
        editor.putString("id", id);
        editor.putString("login", login);
        editor.putString("password", password);
        editor.putBoolean("in", true);
        editor.commit();
        go_to_main();
    }

    @Override
    public void back() {
        go_to_start.run();
        register = new register();
    }

    @Override
    public void error() {

    }

    @Override
    public void go_to_login() {
        go_to_login.run();
    }

    @Override
    public void go_to_register() {
        go_to_register.run();
    }

    @Override
    public void go_to_account_back() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, account_back).addToBackStack(null).commit();
    }

    @Override
    public void go_to_guest_view() {
        go_to_main();
    }

    Runnable go_to_login = new Runnable() {
        @Override
        public void run() {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, login).addToBackStack(null).commit();
        }
    };

    Runnable go_to_register = new Runnable() {
        @Override
        public void run() {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, register).addToBackStack(null).commit();
        }
    };

    Runnable go_to_start = new Runnable() {
        @Override
        public void run() {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, start).commit();
        }
    };

    public void go_to_main(){
        Intent intent = new Intent(this, main.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void school(String number, String type, String name) {
        register.setSchool(name, number, type);
    }
}
