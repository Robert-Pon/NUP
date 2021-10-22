package com.books1.books.main.fragments.profile.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.books.R;
import com.books1.connections.main.profile_c;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class profile_changer extends BottomSheetDialogFragment {
    SharedPreferences sharedPreferences;
    EditText name;
    EditText family_name;
    EditText description;
    EditText city;
    TextView school;
    EditText number;
    EditText email;
    Spinner classes;

    ImageView profile;

    String id, login, password = "";

    List<String> school_list = new ArrayList<>();

    Button update;

    school_bottom_sheet school_bottom_sheet;

    Uri profile_uri = null;

    boolean choose_profile = true;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.edit_profile, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
         id = sharedPreferences.getString("id","-");
         login = sharedPreferences.getString("login","-");
         password = sharedPreferences.getString("password","-");

        name = v.findViewById(R.id.name);
        family_name = v.findViewById(R.id.family_name);
        description = v.findViewById(R.id.description);
        city = v.findViewById(R.id.city);
        school = v.findViewById(R.id.school);
        number = v.findViewById(R.id.number);
        email = v.findViewById(R.id.email);
        classes = v.findViewById(R.id.classes);
        update = v.findViewById(R.id.update);
        profile = v.findViewById(R.id.profile);
        ArrayAdapter<CharSequence> class_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.classes, android.R.layout.simple_spinner_item);
        class_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classes.setAdapter(class_adapter);

        Thread thread = new Thread(){
            @Override
            public void run() {
                StringBuilder data = new StringBuilder();
                data.append("login="+login);
                data.append("&password="+password);
                data.append("&id="+id);
                profile_c profile = new profile_c();
                JSONObject profile_info = profile.get_profile_data(data.toString());
                try {
                    data.append("&school_id="+profile_info.getString("school"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject school1 = profile.get_school(data.toString());
                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            school_list.clear();
                            school_list.add(school1.getString("number"));
                            school_list.add(school1.getString("type"));
                            school_list.add(school1.getString("name"));
                            name.setText(profile_info.getString("name"));
                            family_name.setText(profile_info.getString("familyname"));
                            city.setText(profile_info.getString("city"));
                            school.setText(school1.getString("number")+" "+school1.getString("type")+" "+school1.getString("name"));
                            description.setText(profile_info.getString("description"));
                            number.setText(profile_info.getString("number"));
                            email.setText(profile_info.getString("email"));
                            classes.setSelection(Integer.valueOf(profile_info.getString("class")));
                            if(!profile_info.getString("class").equals(null)){
                                classes.setSelection(Integer.parseInt(profile_info.getString("class")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        };


        thread.start();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread1 = set_data();
                thread1.start();
            }
        });

        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                school_bottom_sheet = new school_bottom_sheet();
                school_bottom_sheet.show(getChildFragmentManager(), "D");
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent,1);
                choose_profile = true;
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            profile_uri = data.getData();
            profile.setImageURI(profile_uri);
            choose_profile = false;
        }catch (Exception e){

        }
    }

    public Thread set_data(){
        return new Thread(){
            @Override
            public void run() {
                super.run();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("login="+login);
                stringBuilder.append("&password="+password);
                stringBuilder.append("&id="+id);
                stringBuilder.append("&name="+name.getText());
                stringBuilder.append("&surname="+family_name.getText());
                stringBuilder.append("&email="+email.getText());
                stringBuilder.append("&city="+city.getText());
                stringBuilder.append("&description="+description.getText());
                stringBuilder.append("&number="+number.getText());
                stringBuilder.append("&class="+classes.getSelectedItemId());

                StringBuilder school_data = new StringBuilder();
                school_data.append("login="+login);
                school_data.append("&password="+password);
                school_data.append("&name="+school_list.get(2).toLowerCase());
                school_data.append("&number="+school_list.get(0).toLowerCase());
                school_data.append("&type="+school_list.get(1).toLowerCase());
                school_data.append("&original="+school.getText());
                Random random = new Random();
                int r1 = random.nextInt(18302);
                Date date = new Date();
                school_data.append("&mID="+String.valueOf(r1)+"="+date.toString());
                List<String> names_of_parameters = new ArrayList<>();
                List<String> values_of_parameters = new ArrayList<>();
                names_of_parameters.add("id");
                names_of_parameters.add("login");
                names_of_parameters.add("password");
                values_of_parameters.add(sharedPreferences.getString("id","-"));
                values_of_parameters.add(sharedPreferences.getString("login","-"));
                values_of_parameters.add(sharedPreferences.getString("password","-"));
                profile_c profile_c = new profile_c();
                if(profile_uri!=null){
                    profile_c.upload_named_photo(profile_uri, getContext(),  "profile.jpg", names_of_parameters, values_of_parameters);
                    profile_c.upload_named_compress_photo(profile_uri, getContext(),  "profile.jpg", names_of_parameters, values_of_parameters);

                }
                profile_c.profile_setter(stringBuilder.toString(),
                        school_data,
                        school.getText().toString()+" "+city.getText().toString(),
                        login,
                        password,
                        getContext());
            }
        };
    }

    public void setSchool(String name, String number, String type){
        school_list.clear();
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
