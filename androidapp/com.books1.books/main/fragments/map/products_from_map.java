package com.books1.books.main.fragments.map;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.books1.connections.main.shop_c;
import com.example.books.R;
import com.books1.books.main.fragments.auctions.shop;
import com.books1.books.urls;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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

public class products_from_map extends BottomSheetDialogFragment {
    RecyclerView items;
    String id, login, password = "";
    SharedPreferences sharedPreferences;

    com.books1.connections.main.shop_c shop_c = new shop_c();

    List<Bitmap> bitmaps = new ArrayList<>();
    shop.shop_in shop_in;
    products_adapter products_adapter;
    List<JSONObject> json_objects = new ArrayList<>();

    double x = 0;
    double y = 0;
    String search = "";

    products_from_map_in products_from_map_in;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.products_form_map, container, false);
        items = v.findViewById(R.id.items);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sharedPreferences.getString("id","");
        login = sharedPreferences.getString("login","");
        password = sharedPreferences.getString("password","");

        products_adapter = new products_adapter(json_objects);
        items.setLayoutManager(new LinearLayoutManager(getContext()));
        items.setAdapter(products_adapter);

        Bundle arguments = this.getArguments();

        x = arguments.getDouble("x");
        y = arguments.getDouble("y");
        search = arguments.getString("search");

        Thread thread = new Thread() {

            @Override
            public void run() {
                StringBuilder data = new StringBuilder();
                data.append("login="+login);
                data.append("&password="+password);
                data.append("&id="+id);
                data.append("&x="+String.valueOf(x));
                data.append("&y="+String.valueOf(y));
                data.append("&search="+search);

                json_objects.addAll(shop_c.get_more_products(data.toString()));
                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ((Activity) getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    products_adapter.notifyDataSetChanged();
                                }
                            });

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
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                products_adapter.notifyItemChanged(bitmaps.size()-1);

                            }
                        });
                    } catch (Exception e) {
                        bitmaps.add(null);
                        e.printStackTrace();
                    }
                }

            }
        };
        thread.start();
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
        public products_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.product_item, parent, false);
            products_adapter.ViewHolder viewHolder = new products_adapter.ViewHolder(view);
            viewHolder.book_profile = view.findViewById(R.id.book_profile);
            viewHolder.book_name = view.findViewById(R.id.book_name);
            viewHolder.subject = view.findViewById(R.id.subject);
            viewHolder.price = view.findViewById(R.id.price);
            viewHolder.product = view.findViewById(R.id.product);


            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull products_adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            try {
                holder.book_name.setText(list.get(position).getString("name"));
                List<String> array = Arrays.asList(getResources().getStringArray(R.array.subjects).clone());
                holder.subject.setText(array.get(Integer.parseInt(list.get(position).getString("subject"))));
                holder.price.setText(list.get(position).getString("price")+" "+list.get(position).getString("currency"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if(bitmaps.get(position)!=null) {
                    holder.book_profile.setImageBitmap(bitmaps.get(position));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }



            holder.product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        products_from_map_in.show_book(list.get(position).getString("id"));
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
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        products_from_map_in = (products_from_map_in) context;
    }

    public interface products_from_map_in{
        public String show_book(String id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bitmaps.clear();
        json_objects.clear();

    }
}
