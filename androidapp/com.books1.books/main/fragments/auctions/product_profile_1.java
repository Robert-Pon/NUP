package com.books1.books.main.fragments.auctions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.R;
import com.books1.books.urls;
import com.books1.connections.main.book_c;
import com.books1.connections.main.profile_c;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class product_profile_1 extends Fragment {

    String id, login, password = "";

    TextView title;
    TextView description;
    TextView price;

    RecyclerView photos;

    Chip class_chip;
    Chip subject_chip;
    Chip level_chip;
    Chip quality_chip;
    Chip pages_chip;
    Chip size_chip;
    Chip profile;
    Chip email_chip;
    Chip number_chip;
    Chip map_chip;

    SharedPreferences sharedPreferences;

    List<Bitmap> bitmaps = new ArrayList<>();

    JSONObject book = new JSONObject();

    photos_adapter photos_adapter;

    Thread get_photos;

    Button buy;
    Button exchange;

    List<String> photos_links = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.product_profile, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sharedPreferences.getString("id","");
        login = sharedPreferences.getString("login","");
        password = sharedPreferences.getString("password","");

        title = v.findViewById(R.id.title);
        description = v.findViewById(R.id.description);
        class_chip = v.findViewById(R.id.class_chip);
        subject_chip = v.findViewById(R.id.subject_chip);
        level_chip = v.findViewById(R.id.level_chip);
        quality_chip = v.findViewById(R.id.quality_chip);
        size_chip = v.findViewById(R.id.size_chip);
        pages_chip = v.findViewById(R.id.pages_chip);
        email_chip = v.findViewById(R.id.email_chip);
        number_chip = v.findViewById(R.id.number_chip);
        price = v.findViewById(R.id.price);
        buy = v.findViewById(R.id.buy);
        exchange = v.findViewById(R.id.exchange_b);
        map_chip = v.findViewById(R.id.map_chip);
        profile = v.findViewById(R.id.profile);

        photos = v.findViewById(R.id.photos);

        Bundle info = this.getArguments();

        photos_adapter = new photos_adapter(bitmaps);
        photos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        photos.setAdapter(photos_adapter);

        get_photos = new Thread(){
            @Override
            public void run() {
                JSONArray list = new JSONArray();
                try{
                    list = new JSONArray(book.getString("photos"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < list.length(); i++) {
                    try {
                        if(!list.getString(i).equals("delete")) {
                            urls urls = new urls();
                            URL url = new URL(urls.ip() + urls.photos_compress_dir() + list.getString(i));
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            InputStream inputStream = httpURLConnection.getInputStream();
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                            Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                            bitmaps.add(bitmap);
                            photos_links.add(list.getString(i));
                            ((Activity) getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    photos_adapter.notifyDataSetChanged();
                                }
                            });
                        }else{
                            photos_links.add("");
                            bitmaps.add(null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread get_information = new Thread(){
            @Override
            public void run() {
                super.run();
                List<String> sizes = Arrays.asList(getResources().getStringArray(R.array.sizes));
                List<String> classes = Arrays.asList(getResources().getStringArray(R.array.classes));
                List<String> subjects = Arrays.asList(getResources().getStringArray(R.array.subjects));

                book_c book_c = new book_c();
                profile_c profile_c = new profile_c();
                StringBuilder data = new StringBuilder();

                data.append("id="+id);
                data.append("&login="+login);
                data.append("&password="+password);
                data.append("&id1="+info.getString("book_id"));
                book = book_c.get_book_by_id(data.toString());

                data = new StringBuilder();
                data.append("id1="+id);
                data.append("&login="+login);
                data.append("&password="+password);
                try {
                    data.append("&id="+book.getString("person_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject profile = profile_c.get_profile_data_to_product(data.toString());
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            title.setText(book.getString("name"));
                            if(!book.getString("description").trim().equals("")){
                                description.setText(book.getString("description"));
                            }else{
                                description.setText("Użytkownik nie dodał opisu !");
                            }

                            String lvl = "", qu = "";
                            switch (book.getString("level")){
                                case "0":
                                    lvl = "Poziom neutralny";
                                    break;
                                case "1":
                                    lvl = "Poziom podstawowy";
                                    break;
                                case "2":
                                    lvl = "Poziom rozszerzony";
                                    break;

                            }

                            switch (book.getString("quality")){
                                case "0":
                                    qu = "Jak nowa";
                                    break;
                                case "1":
                                    qu = "Stan bardzo dobry";
                                    break;
                                case "2":
                                    qu = "Stan dobry";
                                    break;
                                case "3":
                                    qu = "Stan średni";
                                    break;
                                case "4":
                                    qu = "Stan akceptowalny";
                                    break;
                                case "5":
                                    qu = "Stan zły";
                                    break;
                                case "6":
                                    qu = "Stan bardzo zły";
                                    break;

                            }

                            level_chip.setText(lvl);
                            quality_chip.setText(qu);
                            size_chip.setText(sizes.get(Integer.parseInt(book.getString("size"))));
                            class_chip.setText(classes.get(Integer.parseInt(book.getString("class"))));
                            subject_chip.setText(subjects.get(Integer.parseInt(book.getString("subject"))));
                            pages_chip.setText(book.getString("pages"));
                            number_chip.setText(profile.getString("number"));
                            email_chip.setText(profile.getString("email"));
                            price.setText("Cena : "+book.getString("price")+" "+book.getString("currency"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        get_photos.start();
                    }
                });
            }
        };

        get_information.start();

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("book_id", info.getString("book_id"));
                try {
                    bundle.putString("price", book.getString("price"));
                    bundle.putString("currency", book.getString("currency"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                buy_bottom buy_bottom = new buy_bottom();
                buy_bottom.setArguments(bundle);
                buy_bottom.show(getChildFragmentManager(), "d");
            }
        });

        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("book_id", info.getString("book_id"));
                try {
                    bundle.putString("exchange", book.getString("exchange"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                exchange_bottom exchange_bottom = new exchange_bottom();
                exchange_bottom.setArguments(bundle);
                exchange_bottom.show(getChildFragmentManager(), "d");
            }
        });

        map_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread map = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        StringBuilder data = new StringBuilder();
                        data.append("login="+login);
                        data.append("&password="+password);
                        try {
                            data.append("&school="+book.getString("person_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        book_c book_c = new book_c();
                        JSONObject jsonObject = book_c.get_localization(data.toString());
                        Bundle bundle = new Bundle();
                        try {
                            bundle.putDouble("x", Double.parseDouble(jsonObject.getString("x")));
                            bundle.putDouble("y", Double.parseDouble(jsonObject.getString("y")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        bottom_map bottom_map = new bottom_map();
                        bottom_map.setArguments(bundle);
                        bottom_map.show(getChildFragmentManager(), "D");
                    }
                };
                map.start();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
            }
        });

        return v;
    }

    public class photos_adapter extends RecyclerView.Adapter<photos_adapter.ViewHolder>{
        List<Bitmap> links = new ArrayList<>();

        public photos_adapter(List<Bitmap> list){
            this.links = list;
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView photo;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.photo_element_bigger, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            viewHolder.photo = v.findViewById(R.id.image);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if(bitmaps.get(position)!=null) {
                holder.photo.setImageBitmap(bitmaps.get(position));
            }
            holder.photo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(bitmaps.get(position)!=null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("photo", photos_links.get(position));
                        show_img show_img = new show_img();
                        show_img.setArguments(bundle);
                        show_img.show(getChildFragmentManager(), "D");
                    }
                    return false;
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bitmaps.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
