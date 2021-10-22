package com.books1.books.main.fragments.profile;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.books1.books.urls;
import com.books1.connections.main.profile_c;
import com.example.books.R;

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

public class profile extends Fragment {
    String id, login, password = "";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView my_items;
    my_profile my_profile1;
    List<Bitmap> bitmaps = new ArrayList<>();
    products_adapter products_adapter;
    TextView name;
    TextView localization;
    TextView contact;
    TextView description;
    TextView nupses;
    ImageView sync;
    ImageView profile1;
    ImageView settings;
    Thread sync1 = new Thread();
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_profile, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sharedPreferences.getString("id","");
        login = sharedPreferences.getString("login","");
        password = sharedPreferences.getString("password","");
        my_items = v.findViewById(R.id.my_items);
        sync = v.findViewById(R.id.sync);
         name = v.findViewById(R.id.name);
         localization = v.findViewById(R.id.localization);
         contact = v.findViewById(R.id.contact);
         description = v.findViewById(R.id.description);
         nupses = v.findViewById(R.id.nupses);
         profile1 = v.findViewById(R.id.profile1);
         settings = v.findViewById(R.id.settings);

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = (NetworkInfo) connectivityManager.getActiveNetworkInfo();
        boolean connected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        List<JSONObject> json_objects = new ArrayList<>();
        products_adapter = new products_adapter(json_objects);
        my_items.setLayoutManager(new LinearLayoutManager(getContext()));
        my_items.setAdapter(products_adapter);



        if(connected){
            sync1 = sync();
            sync1.start();
        }

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
                LinearInterpolator linearInterpolator = new LinearInterpolator();
                animation.setInterpolator(linearInterpolator);
                animation.setDuration(6000);
                sync.startAnimation(animation);
                if(sync1.isAlive()){
                    sync1.interrupt();
                }
                sync1 = sync();
                sync1.start();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Aby dostać się do ustawień możesz również przeciągnąć w bok !", Toast.LENGTH_LONG).show();
                my_profile1.settings();
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
            ImageView edit;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
        @NonNull
        @Override
        public products_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.my_product_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.book_profile = view.findViewById(R.id.book_profile);
            viewHolder.book_name = view.findViewById(R.id.book_name);
            viewHolder.subject = view.findViewById(R.id.subject);
            viewHolder.price = view.findViewById(R.id.price);
            viewHolder.edit = view.findViewById(R.id.edit);


            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull products_adapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
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

            try {
                if(!list.get(position).getString("reservation").equals("0")) {
                    holder.edit.setImageResource(R.drawable.ic_see);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(list.get(position).getString("reservation").equals("0")) {
                            try {
                                my_profile1.edit_book(list.get(position).getString("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            my_profile1.show_book_1(list.get(position).getString("id"));
                        }
                    } catch (Exception e) {
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
        my_profile1 = (my_profile) context;
    }

    public interface my_profile{
        public void edit_book(String book_id);
        public String show_book_1(String book_id);
        public void settings();
    }

    public Thread sync(){
        return new Thread() {

            @Override
            public void run() {
                StringBuilder data = new StringBuilder();
                data.append("login="+login);
                data.append("&password="+password);
                data.append("&id="+id);
                profile_c profile = new profile_c();
                JSONObject profile_info = profile.get_profile_data(data.toString());
                List<JSONObject> json_objects = profile.get_my_products(data.toString());
                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {


                            name.setText(profile_info.getString("name") + " " + profile_info.getString("familyname"));
                            localization.setText(profile_info.getString("city") + ", " + profile_info.getString("school"));
                            contact.setText(profile_info.getString("email") + ", " + profile_info.getString("number"));
                            description.setText(profile_info.getString("description"));
                            nupses.setText(profile_info.getString("nups")+" nupsów");
                            products_adapter = new products_adapter(json_objects);
                            my_items.setLayoutManager(new LinearLayoutManager(getContext()));
                            my_items.setAdapter(products_adapter);



                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                        }



                    }
                });
                try {
                urls urls = new urls();
                URL url = null;

                    url = new URL(urls.ip() + urls.photos_compress_dir_prof()+"/"+id+"/profile.jpg");

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
    }
}
