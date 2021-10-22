package com.books1.books.main.fragments.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.books1.connections.main.add_book_c;
import com.books1.connections.main.book_c;
import com.example.books.R;
import com.books1.books.urls;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class edit_book extends BottomSheetDialogFragment {
    String id, id1, login, password = "";

    Button save;
    Button delete;
    Button cancel;

    EditText name;
    EditText description;
    EditText price;
    EditText pages;

    ChipGroup level;
    ChipGroup quality;
    edit_book_in edit_book_in;

    Chip l1;
    Chip l2;
    Chip l3;
    Chip q1;
    Chip q2;
    Chip q3;
    Chip q4;
    Chip q5;
    Chip q6;
    Chip q7;

    boolean choose_profile = false;

    SharedPreferences sharedPreferences;

    new_photos_adapter new_photos_adapter;
    photos_adapter photos_adapter;

    List<String> links = new ArrayList<>();
    List<Bitmap> bitmaps = new ArrayList<>();

    JSONObject book = new JSONObject();

    RecyclerView uploaded_photos;
    RecyclerView new_photos;

    Uri profile_uri = null;

    JSONArray uploaded_photos_links = new JSONArray();
    List<String> uploaded_photos_links_list = new ArrayList<>();

    ImageView add_photo;
    ImageView profile;

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_book, container, false);
        save = v.findViewById(R.id.add);
        delete = v.findViewById(R.id.delete);
        cancel = v.findViewById(R.id.cancel);

        Spinner classes = v.findViewById(R.id.class_spinner);
        Spinner sizes = v.findViewById(R.id.size_spinner);
        Spinner subject = v.findViewById(R.id.subject_spinner);

        name = v.findViewById(R.id.name);
        description = v.findViewById(R.id.description);
        price = v.findViewById(R.id.price);
        pages = v.findViewById(R.id.quantity);

        level = v.findViewById(R.id.level_chip_g);
        quality = v.findViewById(R.id.quality_chip_g);

        l1 = v.findViewById(R.id.l1_chip);
        l2 = v.findViewById(R.id.l2_chip);
        l3 = v.findViewById(R.id.l3_chip);
        q1 = v.findViewById(R.id.q1_chip);
        q2 = v.findViewById(R.id.q2_chip);
        q3 = v.findViewById(R.id.q3_chip);
        q4 = v.findViewById(R.id.q4_chip);
        q5 = v.findViewById(R.id.q5_chip);
        q6 = v.findViewById(R.id.q6_chip);
        q7 = v.findViewById(R.id.q7_chip);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        Bundle bundle = this.getArguments();

        id = sharedPreferences.getString("id","");
        id1 = bundle.getString("id", "");
        login = sharedPreferences.getString("login","");
        password = sharedPreferences.getString("password","");

        add_photo = v.findViewById(R.id.add_photo);
        profile = v.findViewById(R.id.profile);

        uploaded_photos = v.findViewById(R.id.uploaded_photos);
        new_photos = v.findViewById(R.id.photos);

        ArrayAdapter<CharSequence> class_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.classes, android.R.layout.simple_spinner_item);
        class_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classes.setAdapter(class_adapter);

        ArrayAdapter<CharSequence> sizes_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sizes, android.R.layout.simple_spinner_item);
        class_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizes.setAdapter(sizes_adapter);

        ArrayAdapter<CharSequence> subject_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.subjects, android.R.layout.simple_spinner_item);
        class_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject.setAdapter(subject_adapter);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


        photos_adapter = new photos_adapter(new ArrayList<>());
        uploaded_photos.setLayoutManager(linearLayoutManager);
        uploaded_photos.setAdapter(photos_adapter);

        new_photos_adapter = new new_photos_adapter(new ArrayList<>());
        new_photos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        new_photos.setAdapter(new_photos_adapter);


        Thread get_photos = new Thread(){
            @Override
            public void run() {
                JSONArray list = new JSONArray();
                try{
                    list = new JSONArray(book.getString("photos"));
                    uploaded_photos_links = new JSONArray(book.getString("photos"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int i = 0;
                try {
                    if(list.getString(0).equals("/"+id+"/"+id1+"/profile.jpg")) {
                        i = 1;
                        uploaded_photos_links_list.add(list.getString(0));
                        urls urls = new urls();
                        URL url = new URL(urls.ip() + urls.photos_compress_dir() + list.getString(0));
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                profile.setImageBitmap(bitmap);
                            }
                        });
                    }
              } catch (Exception e) {
                    e.printStackTrace();
                }
                for (i = 1; i < list.length(); i++) {
                    try {
                        uploaded_photos_links_list.add(list.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if(!list.getString(i).equals("delete")) {
                            urls urls = new urls();
                            URL url = new URL(urls.ip() + urls.photos_compress_dir() + list.getString(i));
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            InputStream inputStream = httpURLConnection.getInputStream();
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                            Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                            bitmaps.add(bitmap);
                            ((Activity) getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    photos_adapter.notifyDataSetChanged();
                                }
                            });
                        }else{
                            bitmaps.add(null);
                            ((Activity) getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    photos_adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }



            }
        };

        Thread thread = new Thread(){
            @Override
            public void run() {
                book_c book_c = new book_c();
                StringBuilder data = new StringBuilder();
                data.append("login="+login);
                data.append("&password="+password);
                data.append("&id="+id);
                data.append("&id1="+id1);
                book = book_c.get_book_by_id(data.toString());
                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            name.setText(book.getString("name"));
                            description.setText(book.getString("description"));
                            price.setText(book.getString("price"));
                            pages.setText((book.getString("pages")));
                            classes.setSelection(Integer.parseInt(book.getString("class")));
                            sizes.setSelection(Integer.parseInt(book.getString("size")));
                            subject.setSelection(Integer.parseInt(book.getString("subject")));
                            switch (book.getString("level")){
                                case "0":
                                    l1.setChecked(true);
                                    break;
                                 case "1":
                                    l2.setChecked(true);
                                    break;
                                 case "2":
                                    l3.setChecked(true);
                                    break;

                            }
                            switch (book.getString("quality")){
                                case "0":
                                    q1.setChecked(true);
                                    break;
                                 case "1":
                                    q2.setChecked(true);
                                    break;
                                 case "2":
                                    q3.setChecked(true);
                                    break;
                                    case "3":
                                    q4.setChecked(true);
                                    break;
                                 case "4":
                                    q5.setChecked(true);
                                    break;
                                 case "5":
                                    q6.setChecked(true);
                                    break;
                                    case "6":
                                    q7.setChecked(true);
                                    break;

                            }



                        }catch (Exception e){

                        }

                        get_photos.start();
                    }
                });


            }
        };
        thread.start();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().trim().equals("")){
                    if(!price.getText().toString().equals(null)){
                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                book_c book_c = new book_c();
                                bottom_sheet_uploading bottom_sheet_uploading = new bottom_sheet_uploading();
                                bottom_sheet_uploading.show(getChildFragmentManager(), "D");
                                Chip level_chip = level.findViewById(level.getCheckedChipId());
                                Chip quality_chip = quality.findViewById(quality.getCheckedChipId());
                                StringBuilder data = new StringBuilder();
                                try{
                                    bottom_sheet_uploading.setText("Aktualizacja danych");

                                }catch (Exception e){

                                }
                                data.append("name="+name.getText().toString());
                                data.append("&class="+String.valueOf(classes.getSelectedItemId()));
                                data.append("&subject="+String.valueOf(subject.getSelectedItemId()));
                                data.append("&description="+description.getText().toString());
                                String lID = "0";
                                String qID = "0";
                                switch (level_chip.getText().toString()){
                                    case "Poziom neutralny":
                                        lID = "0";
                                        break;
                                    case "Poziom podstawowy":
                                        lID = "1";
                                        break;
                                    case "Poziom rozszerzony":
                                        lID = "2";
                                        break;

                                }
                                switch (quality_chip.getText().toString()){
                                    case "Jak noaw":
                                        qID = "0";
                                        break;
                                    case "Stan bardzo dobry":
                                        qID = "1";
                                        break;
                                    case "Stan dobry":
                                        qID = "2";
                                        break;
                                    case "Stan średni":
                                        qID = "3";
                                        break;
                                    case "Stan akceptowalny":
                                        qID = "4";
                                        break;
                                    case "Stan zły":
                                        qID = "5";
                                        break;
                                    case "Stan bardzo zły":
                                        qID = "6";
                                        break;

                                }
                                data.append("&level="+lID);
                                data.append("&quality="+qID);
                                data.append("&price="+price.getText().toString());
                                data.append("&pages="+pages.getText().toString());
                                String finallist = "[";
                                for(int i = 0; i < uploaded_photos_links_list.size(); i++){
                                    finallist+="\"";
                                    finallist+=uploaded_photos_links_list.get(i);
                                    finallist+="\"";
                                    if(i+1<uploaded_photos_links_list.size()){
                                        finallist+=",";
                                    }
                                }
                                finallist+="]";
                                data.append("&photos="+ finallist);
                                data.append("&size="+String.valueOf(sizes.getSelectedItemId()));
                                data.append("&id="+sharedPreferences.getString("id","-"));
                                data.append("&login="+sharedPreferences.getString("login","-"));
                                data.append("&password="+sharedPreferences.getString("password","-"));
                                data.append("&too_search="+level_chip.getText().toString()+quality_chip.getText().toString()+name.getText().toString()+String.valueOf(subject.getSelectedItem())+String.valueOf(classes.getSelectedItem()));
                                data.append("&id1="+id1);
                                book_c.edit_book(data.toString());

                                List<String> names_of_parameters = new ArrayList<>();
                                List<String> values_of_parameters = new ArrayList<>();
                                names_of_parameters.add("id");
                                names_of_parameters.add("login");
                                names_of_parameters.add("password");
                                names_of_parameters.add("mID");
                                values_of_parameters.add(sharedPreferences.getString("id","-"));
                                values_of_parameters.add(sharedPreferences.getString("login","-"));
                                values_of_parameters.add(sharedPreferences.getString("password","-"));

                                try {
                                    values_of_parameters.add(book.getString("id"));
                                }catch (Exception e){

                                }
                                bottom_sheet_uploading.setText("Aktualizacja profilu");
                                add_book_c add_book_c = new add_book_c();
                                if(profile_uri!=null){
                                    add_book_c.upload_named_photo(profile_uri, getContext(),  "profile.jpg", names_of_parameters, values_of_parameters);
                                    add_book_c.upload_named_compress_photo(profile_uri, getContext(),  "profile.jpg", names_of_parameters, values_of_parameters);

                                }

                                int i = 0;
                                bottom_sheet_uploading.setText("Aktualizacja zdęć");

                                for (String uri: links) {
                                    i++;
                                    add_book_c.upload_photo(Uri.parse(uri), getContext(), data.toString(), names_of_parameters, values_of_parameters);
                                    add_book_c.upload_compress_photo(Uri.parse(uri), getContext(), data.toString(), names_of_parameters, values_of_parameters);
                                }
                                bottom_sheet_uploading.setText("Aktualizacja zakończona");

                                ((Activity) getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        edit_book_in.save_book();
                                    }
                                });

                            }
                        };
                        thread.start();
                    }else{
                        Toast.makeText(getContext(), "Brak ceny !",Toast.LENGTH_LONG);
                    }
                }else {
                    Toast.makeText(getContext(), "Brak tytułu !", Toast.LENGTH_LONG);
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_book_in.cancel_book();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        book_c book_c = new book_c();
                        StringBuilder data = new StringBuilder();
                        data.append("login="+login);
                        data.append("&password="+password);
                        data.append("&id="+id);
                        data.append("&id1="+id1);
                        book_c.delete_book(data.toString());
                    }
                };
                thread.start();
                edit_book_in.delete_book();
            }
        });

        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent,1);
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
            if (!choose_profile) {
                Uri uri = data.getData();
                links.add(uri.toString());
                new_photos_adapter.update(links);
            }else{
                profile_uri = data.getData();
                profile.setImageURI(profile_uri);
                choose_profile = false;
            }
        }catch (Exception e){
        }
    }


    public class photos_adapter extends RecyclerView.Adapter<photos_adapter.ViewHolder>{
        List<Bitmap> links = new ArrayList<>();

        public photos_adapter(List<Bitmap> list){
            this.links = list;
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView photo;
            Button delete;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

        @NonNull
        @Override
        public photos_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.photo_element, parent, false);
            photos_adapter.ViewHolder viewHolder = new photos_adapter.ViewHolder(v);
            viewHolder.photo = v.findViewById(R.id.image);
            viewHolder.delete = v.findViewById(R.id.delete);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull photos_adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if(bitmaps.get(position)!=null) {
                holder.photo.setImageBitmap(bitmaps.get(position));
            }

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    uploaded_photos_links_list.set(position+1, "delete");
                    holder.photo.setImageResource(R.drawable.ic_book);
                }
            });

        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return bitmaps.size();
        }


    }


    public class new_photos_adapter extends RecyclerView.Adapter<new_photos_adapter.ViewHolder>{
        List<String> links = new ArrayList<>();

        public new_photos_adapter(List<String> list){
            this.links = list;
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView photo;
            Button delete;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

        public void update(List<String> links){
            this.links = links;
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public new_photos_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.photo_element, parent, false);
            new_photos_adapter.ViewHolder viewHolder = new new_photos_adapter.ViewHolder(v);
            viewHolder.photo = v.findViewById(R.id.image);
            viewHolder.delete = v.findViewById(R.id.delete);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull new_photos_adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.photo.setImageURI(Uri.parse(links.get(position)));
            Thread thread = new Thread(){
                @Override
                public void run() {
                    super.run();
                    Bitmap bitmap = ((BitmapDrawable) holder.photo.getDrawable()).getBitmap();
                    final Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 200, 250, true);

                    ((Activity)getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.photo.setImageBitmap(scaled);

                        }
                    });
                    bitmap = null;


                }
            };
            holder.photo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("uri", links.get(position));
//                    show.setArguments(bundle);
//                    show.show(getFragmentManager(), "d");
                    return false;
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    links.remove(position);
                    new_photos_adapter.notifyItemRemoved(position);
                }
            });
            thread.start();
        }

        @Override
        public int getItemCount() {
            return this.links.size();
        }


    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        edit_book_in = (edit_book_in) context;
    }

    public interface edit_book_in{
        public void save_book();
        public void cancel_book();
        public void delete_book();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bitmaps.clear();
        links.clear();
        uploaded_photos_links_list.clear();
    }
}
