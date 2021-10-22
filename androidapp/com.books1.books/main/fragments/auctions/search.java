package com.books1.books.main.fragments.auctions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.books1.connections.main.shop_c;
import com.example.books.R;
import com.books1.books.urls;

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

public class search extends Fragment {
    ImageView search_b;
    EditText search;

    RecyclerView items;
    RecyclerView profiles;

    String id, login, password = "";
    SharedPreferences sharedPreferences;

   com.books1.connections.main.shop_c shop_c = new shop_c();

   search_in search_in;

    List<Bitmap> bitmaps = new ArrayList<>();
    List<JSONObject> json_objects;
    List<Bitmap> bitmaps1 = new ArrayList<>();
    List<JSONObject> json_objects1;

    Thread thread2;
    Thread thread3;
    products_adapter products_adapter;
    profiles_adapter profiles_adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_layout, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sharedPreferences.getString("id","");
        login = sharedPreferences.getString("login","");
        password = sharedPreferences.getString("password","");

        items = v.findViewById(R.id.items);
        profiles = v.findViewById(R.id.profiles);

        search_b = v.findViewById(R.id.search_b);
        search = v.findViewById(R.id.search);

        json_objects = new ArrayList<>();
        products_adapter = new products_adapter(json_objects);
        items.setLayoutManager(new LinearLayoutManager(getContext()));
        items.setAdapter(products_adapter);

        profiles_adapter = new profiles_adapter(json_objects);
        profiles.setLayoutManager(new LinearLayoutManager(getContext()));
        profiles.setAdapter(products_adapter);



        Thread thread1 = new Thread(){
            @Override
            public void run() {

                StringBuilder data = new StringBuilder();
                data.append("login="+login);
                data.append("&password="+password);
                data.append("&id="+id);
                json_objects = shop_c.get_products(data.toString());
                for (JSONObject json_object: json_objects) {
                    JSONArray photos = null;
                    try {
                        photos = new JSONArray(json_object.getString("photos"));
                        urls urls = new urls();
                        URL url = new URL(urls.ip() + urls.photos_compress_dir() + photos.getString(0));
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                        bitmaps.add(bitmap);
                        products_adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        bitmaps.add(null);
                    }
                }

            }
        };

        Thread thread = new Thread() {

            @Override
            public void run() {
                StringBuilder data = new StringBuilder();
                data.append("login="+login);
                data.append("&password="+password);
                data.append("&id="+id);
                data.append("&search="+search.getText().toString());
                json_objects = shop_c.search(data.toString());
                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            products_adapter products_adapter = new products_adapter(json_objects);
                            items.setLayoutManager(new LinearLayoutManager(getContext()));
                            items.setAdapter(products_adapter);

                            thread1.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }
                });


            }
        };

         thread2 = new Thread();
         thread3 = new Thread();
        search_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thread2.isAlive()){
                    thread2.interrupt();
                }

                thread2 = new_thread();
                thread2.start();
                if(thread3.isAlive()) {
                    thread3.interrupt();
                }
                thread3 = get_profiles();
                thread3.start();

            }
        });

        return v;
    }


    public class products_adapter extends RecyclerView.Adapter<products_adapter.ViewHolder>{

        List<JSONObject> list;

        public products_adapter(List<JSONObject> list){
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView book_profile;
            TextView book_name;
            TextView subject;
            TextView price;
            CardView product;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.my_product_item_2, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.book_profile = view.findViewById(R.id.book_profile);
            viewHolder.book_name = view.findViewById(R.id.book_name);
            viewHolder.subject = view.findViewById(R.id.subject);
            viewHolder.price = view.findViewById(R.id.price);
            viewHolder.product = view.findViewById(R.id.product);


            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            try {
                holder.book_name.setText(list.get(position).getString("name"));
                List<String> array = Arrays.asList(getResources().getStringArray(R.array.subjects).clone());
                holder.subject.setText(array.get(Integer.parseInt(list.get(position).getString("subject"))));
                holder.price.setText(list.get(position).getString("price")+" PLN");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                if(bitmaps.get(position)!=null) {
                    holder.book_profile.setImageBitmap(bitmaps.get(position));

                }
            } catch (Exception e) {

            }
            holder.product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        search_in.show_book_1(list.get(position).getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
            return list.size();
        }


    }

public class profiles_adapter extends RecyclerView.Adapter<profiles_adapter.ViewHolder>{

        List<JSONObject> list;

        public profiles_adapter(List<JSONObject> list){
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView book_profile;
            TextView book_name;
            TextView surname;
            TextView city;
            CardView product;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.my_product_item_2, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.book_profile = view.findViewById(R.id.book_profile);
            viewHolder.book_name = view.findViewById(R.id.book_name);
            viewHolder.surname = view.findViewById(R.id.subject);
            viewHolder.city = view.findViewById(R.id.price);
            viewHolder.product = view.findViewById(R.id.product);


            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            try {
                holder.book_name.setText(list.get(position).getString("name"));
                holder.surname.setText(list.get(position).getString("familyname"));
                holder.city.setText(list.get(position).getString("city"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                if(bitmaps.get(position)!=null) {
                    holder.book_profile.setImageBitmap(bitmaps.get(position));

                }
            } catch (Exception e) {

            }
            holder.product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        search_in.go_to_profile_1(list.get(position).getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
            return list.size();
        }


    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        search_in = (search_in) context;
    }

    public interface  search_in{
        public String show_book_1(String id);
        public void go_to_profile_1(String id);
    }

    public Thread new_thread(){
        return new Thread() {

            @Override
            public void run() {
                StringBuilder data = new StringBuilder();
                data.append("login="+login);
                data.append("&password="+password);
                data.append("&id="+id);
                data.append("&search="+search.getText().toString());
                List<JSONObject> json_objects = shop_c.search(data.toString());
                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            products_adapter = new products_adapter(json_objects);
                            items.setLayoutManager(new LinearLayoutManager(getContext()));
                            items.setAdapter(products_adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }
                });
                for (JSONObject json_object: json_objects) {
                    JSONArray photos = null;
                    try {
                        photos = new JSONArray(json_object.getString("photos"));
                        urls urls = new urls();
                        URL url = new URL(urls.ip() + urls.photos_compress_dir() + photos.getString(0));
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                        bitmaps.add(bitmap);
                        ((Activity)getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                products_adapter.notifyDataSetChanged();
                            }
                        });
                    } catch (Exception e) {
                        bitmaps.add(null);
                    }
                }


            }
        };
    }

    public Thread get_profiles(){
        return new Thread() {

            @Override
            public void run() {
                StringBuilder data = new StringBuilder();
                data.append("login="+login);
                data.append("&password="+password);
                data.append("&id="+id);
                data.append("&search="+search.getText().toString());
                List<JSONObject> json_objects = shop_c.search_profiles(data.toString());
                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            profiles_adapter profiles_adapter = new profiles_adapter(json_objects);
                            profiles.setLayoutManager(new LinearLayoutManager(getContext()));
                            profiles.setAdapter(profiles_adapter);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }
                });
                for (JSONObject json_object: json_objects) {
                    JSONArray photos = null;
                    try {
                        photos = new JSONArray(json_object.getString("photos"));
                        urls urls = new urls();
                        URL url = new URL(urls.ip() + urls.photos_compress_dir() + photos.getString(0));
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                        bitmaps.add(bitmap);
                        profiles_adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        bitmaps.add(null);
                    }
                }

            }
        };
    }



}
