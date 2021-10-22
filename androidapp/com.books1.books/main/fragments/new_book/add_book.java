package com.books1.books.main.fragments.new_book;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.books1.connections.main.add_book_c;
import com.example.books.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class add_book extends Fragment{
    EditText name;
    EditText description;
    EditText price;
    EditText pages;
    EditText exchange;

    ChipGroup level;
    ChipGroup quality;

    SharedPreferences sharedPreferences;

    ImageView add_photo;
    ImageView profile;

    photos_adapter photos_adapter;

    RecyclerView photos;

    static List<String> links = new ArrayList<>();

    show_img show = new show_img();

    Uri profile_uri = null;

    boolean choose_profile = false;

    add_book_in add_book_in;

    TextView tags;

    List<String> tags_id = new ArrayList<>();

    com.books1.books.main.fragments.new_book.bottom_sheet_uploading bottom_sheet_uploading;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_book, container, false);
        Spinner classes = v.findViewById(R.id.class_spinner);
        Spinner sizes = v.findViewById(R.id.size_spinner);
        Spinner subject = v.findViewById(R.id.subject_spinner);

        name = v.findViewById(R.id.name);
        description = v.findViewById(R.id.description);
        price = v.findViewById(R.id.price);
        pages = v.findViewById(R.id.quantity);
        add_photo = v.findViewById(R.id.add_photo);
        photos = v.findViewById(R.id.photos);
        profile = v.findViewById(R.id.profile);
        exchange = v.findViewById(R.id.exchange);
        tags = v.findViewById(R.id.tags);

        level = v.findViewById(R.id.level_chip_g);
        quality = v.findViewById(R.id.quality_chip_g);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
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

        Button add = v.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().trim().equals("")){
                    if( !price.getText().toString().equals(null)){
                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                bottom_sheet_uploading = new bottom_sheet_uploading();
                                bottom_sheet_uploading.show(getChildFragmentManager(), "D");
                                add_book_c add_book_c = new add_book_c();
                                Chip level_chip = level.findViewById(level.getCheckedChipId());
                                Chip quality_chip = quality.findViewById(quality.getCheckedChipId());
                                StringBuilder data = new StringBuilder();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        bottom_sheet_uploading.setText("Wysłanie danych książki");
                                    }
                                });
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
                                    case "Jak nowa":
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
                                String finallist = "[";
                                for(int i = 0; i < tags_id.size(); i++){
                                    finallist+="\"";
                                    finallist+=tags_id.get(i);
                                    finallist+="\"";
                                    if(i+1<tags_id.size()){
                                        finallist+=",";
                                    }
                                }
                                finallist+="]";
                                data.append("&level="+lID);
                                data.append("&tags="+finallist);
                                data.append("&quality="+qID);
                                data.append("&exchange="+exchange.getText().toString());
                                data.append("&price="+price.getText().toString());
                                data.append("&pages="+pages.getText().toString());
                                data.append("&size="+String.valueOf(sizes.getSelectedItemId()));
                                data.append("&id="+sharedPreferences.getString("id","-"));
                                data.append("&login="+sharedPreferences.getString("login","-"));
                                data.append("&password="+sharedPreferences.getString("password","-"));
                                data.append("&too_search="+level_chip.getText().toString()+quality_chip.getText().toString()+name.getText().toString()+String.valueOf(subject.getSelectedItemId())+String.valueOf(classes.getSelectedItemId()));
                                Random random = new Random();
                                int r1 = random.nextInt(18302);
                                Date date = new Date();
                                data.append("&mID="+String.valueOf(r1)+"="+date.toString());
                                JSONObject add_book_response = add_book_c.add_book(data.toString());
                                String mID = "";
                                try {
                                     mID = add_book_response.getString("id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                data = new StringBuilder();
                                List<String> names_of_parameters = new ArrayList<>();
                                List<String> values_of_parameters = new ArrayList<>();
                                names_of_parameters.add("id");
                                names_of_parameters.add("login");
                                names_of_parameters.add("password");
                                names_of_parameters.add("mID");
                                values_of_parameters.add(sharedPreferences.getString("id","-"));
                                values_of_parameters.add(sharedPreferences.getString("login","-"));
                                values_of_parameters.add(sharedPreferences.getString("password","-"));
                                values_of_parameters.add(mID);
                                bottom_sheet_uploading.setText("Wysłanie zdjęć");
                                if(profile_uri!=null){
                                    add_book_c.upload_named_photo(profile_uri, getContext(),  "profile.jpg", names_of_parameters, values_of_parameters);
                                    add_book_c.upload_named_compress_photo(profile_uri, getContext(),  "profile.jpg", names_of_parameters, values_of_parameters);
                                    bottom_sheet_uploading.setText("Wysłano zdjęcie profilowe !");

                                }
                                int i = 0;
                                for (String uri: links) {
                                    i++;
                                    add_book_c.upload_photo(Uri.parse(uri), getContext(), data.toString(), names_of_parameters, values_of_parameters);
                                    add_book_c.upload_compress_photo(Uri.parse(uri), getContext(), data.toString(), names_of_parameters, values_of_parameters);
                                    bottom_sheet_uploading.setText("Wysłano " + String.valueOf(i)+" zdjęcie");
                                }
                                ((Activity)getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        name.setText("");
                                        description.setText("");
                                        price.setText("");
                                        pages.setText("");
                                    }
                                });
                               bottom_sheet_uploading.dismiss();
                                add_book_in.uploaded();
                            }
                        };
                        thread.start();
                    }else{
                        Toast.makeText(getContext(), "Brak ceny !",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Brak nazwy !",Toast.LENGTH_LONG).show();
                }
            }
        });
        photos_adapter = new photos_adapter(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        photos.setLayoutManager(linearLayoutManager);
        photos.setAdapter(photos_adapter);

        Thread runnable = new Thread() {
            @Override
            public void run() {
                photos_adapter = new photos_adapter(links);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                photos.setLayoutManager(linearLayoutManager);
                photos.setAdapter(photos_adapter);
            }
        };
        runnable.start();


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

        tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tags tags = new tags();

                tags.show(getChildFragmentManager(), "D");
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
                photos_adapter.update(links);
            }else{
                profile_uri = data.getData();
                profile.setImageURI(profile_uri);
                choose_profile = false;
            }
        }catch (Exception e){
        }
    }



    public class photos_adapter extends RecyclerView.Adapter<photos_adapter.ViewHolder>{
        List<String> links = new ArrayList<>();

        public photos_adapter(List<String> list){
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
        public photos_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.photo_element, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            viewHolder.photo = v.findViewById(R.id.image);
            viewHolder.delete = v.findViewById(R.id.delete);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull photos_adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
                    Bundle bundle = new Bundle();
                    bundle.putString("uri", links.get(position));
                    show.setArguments(bundle);
                    show.show(getFragmentManager(), "d");
                    return false;
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    links.remove(position);
                    photos_adapter.notifyItemRemoved(position);
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
        add_book_in = (add_book_in) context;
    }

    public interface add_book_in{
        public void uploaded();
    }

    public void tags_setter(List<String> list){
        tags_id = list;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        links.clear();
    }
}
