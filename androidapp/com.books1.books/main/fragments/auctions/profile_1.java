package com.books1.books.main.fragments.auctions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.books1.connections.main.book_c;
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

public class profile_1 extends Fragment {
    String id, login, password, id1 = "";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView my_items;
    List<Bitmap> bitmaps = new ArrayList<>();
    products_adapter products_adapter;
    profile_1_in profile_1_in;
    ImageView profile1;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sharedPreferences.getString("id","");
        login = sharedPreferences.getString("login","");
        password = sharedPreferences.getString("password","");
        id1 = sharedPreferences.getString("id1","");
        my_items = v.findViewById(R.id.my_items);
        profile1 = v.findViewById(R.id.profile1);

        book_c book_c = new book_c();
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = (NetworkInfo) connectivityManager.getActiveNetworkInfo();
        boolean connected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        List<JSONObject> json_objects = new ArrayList<>();
        products_adapter = new products_adapter(json_objects);
        my_items.setLayoutManager(new LinearLayoutManager(getContext()));
        my_items.setAdapter(products_adapter);

        Thread thread1 = new Thread(){
            @Override
            public void run() {
                StringBuilder data = new StringBuilder();
                data.append("login="+login);
                data.append("&password="+password);
                data.append("&id="+id);
                data.append("&id1="+id1);
                JSONObject profile_info = book_c.get_profile_data(data.toString());
                try {
                    urls urls = new urls();
                    URL url = null;

                    url = new URL(urls.ip() + urls.photos_compress_dir_prof()+"/"+profile_info.getString("id")+"/profile.jpg");

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            profile1.setImageBitmap(bitmap);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                data = new StringBuilder();
                data.append("login="+login);
                data.append("&password="+password);
                data.append("&id="+id);
                data.append("&id1="+id1);
                List<JSONObject> json_objects = book_c.get_products(data.toString());
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
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                products_adapter.notifyDataSetChanged();
                            }
                        });

                    } catch (Exception e) {
                        bitmaps.add(null);
                        e.printStackTrace();
                    }
                }

            }
        };

        if(connected){
            Thread thread = new Thread() {

                @Override
                public void run() {
                    StringBuilder data = new StringBuilder();
                    data.append("login="+login);
                    data.append("&password="+password);
                    data.append("&id="+id);
                    data.append("&id1="+id1);
                    JSONObject profile_info = book_c.get_profile_data(data.toString());
                    List<JSONObject> json_objects = book_c.get_products(data.toString());
                        ((Activity)getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    TextView name = v.findViewById(R.id.name);
                                    TextView localization = v.findViewById(R.id.localization);
                                    TextView contact = v.findViewById(R.id.contact);
                                    TextView description = v.findViewById(R.id.description);
TextView nupses = v.findViewById(R.id.nupses);
                                    name.setText(profile_info.getString("name") + " " + profile_info.getString("familyname"));
                                    localization.setText(profile_info.getString("city") + ", " + profile_info.getString("school"));
                                    contact.setText(profile_info.getString("email") + ", " + profile_info.getString("number"));
                                    description.setText(profile_info.getString("description"));
                                    nupses.setText(profile_info.getString("nups")+" nupsów");

                                    products_adapter = new products_adapter(json_objects);
                                    my_items.setLayoutManager(new LinearLayoutManager(getContext()));
                                    my_items.setAdapter(products_adapter);

                                    thread1.start();
                                } catch (
                                        JSONException e) {
                                    e.printStackTrace();
                                }



                            }
                        });


                }
            };
            thread.start();

        }

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
                List<String> array = Arrays.asList(getResources().getStringArray(R.array.subjects).clone());
                holder.subject.setText(array.get(Integer.parseInt(list.get(position).getString("subject"))));
                holder.price.setText(list.get(position).getString("price") + " " + list.get(position).getString("currency"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                holder.book_name.setText(list.get(position).getString("name"));
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
                        profile_1_in.show_book_2(list.get(position).getString("id"));
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
        profile_1_in = (profile_1_in) context;
    }

    public interface profile_1_in{
        public void show_book_2(String id);
    }


}
