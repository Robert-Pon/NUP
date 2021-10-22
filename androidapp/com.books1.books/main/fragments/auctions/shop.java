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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
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

public class shop extends Fragment {
    String id, login, password = "";
    SharedPreferences sharedPreferences;

    RecyclerView items;

    com.books1.connections.main.shop_c shop_c = new shop_c();

    List<Bitmap> bitmaps = new ArrayList<>();

    shop_in shop_in;

    TextView search;
    products_adapter products_adapter;
    Thread thread1 = null;
    Thread thread = null;
    List<JSONObject> json_objects = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.shop, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sharedPreferences.getString("id","");
        login = sharedPreferences.getString("login","");
        password = sharedPreferences.getString("password","");
        search = v.findViewById(R.id.search);
        items = v.findViewById(R.id.items);
        products_adapter = new products_adapter(json_objects);
        //items.addItemDecoration(new Grid);
        //items.addItemDecoration(new Margin(this));
        //items.setHasFixedSize(true);
        //items.setLayoutManager(new GridLayoutManager(getContext(), 2));

        items.setAdapter(products_adapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop_in.search();
            }
        });



        thread = download();
        if (json_objects.size()==0){
            thread.start();
        }
        items.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!items.canScrollVertically(1)) {
                    if(thread.isAlive()){
                    }else{
                        //thread.interrupt();
                        if(json_objects.size()-1>=0) {
                            try {

                                    thread = download_more(json_objects.get(json_objects.size() - 1).getString("id"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            thread.start();
                        }
                        Toast.makeText(getContext(), "Pobieranie kolejnych ogłoszeń", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public class products_adapter extends RecyclerView.Adapter<products_adapter.ViewHolder>{

        List<JSONObject> list;

        public products_adapter(List<JSONObject> list){
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView book_profile;
            TextView book_name;
            TextView price;
            TextView subject;
            CardView product;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
        @NonNull
        @Override
        public products_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.product_item_new, parent, false);
            products_adapter.ViewHolder viewHolder = new products_adapter.ViewHolder(view);
            viewHolder.book_profile = view.findViewById(R.id.book_profile);
            viewHolder.book_name = view.findViewById(R.id.name);
            viewHolder.price = view.findViewById(R.id.price);
            viewHolder.subject = view.findViewById(R.id.subject);
            viewHolder.product = view.findViewById(R.id.product);


            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull products_adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

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
                e.printStackTrace();
            }



            holder.product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        shop_in.show_book(list.get(position).getString("id"));
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
        shop_in = (shop_in) context;
    }

    public interface  shop_in{
        public String show_book(String id);
        public void search();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        try {
            if (thread1.isAlive()){
                thread1.interrupt();
            }
        }catch (Exception e){}
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    public Thread download(){
        return new Thread() {

            @Override
            public void run() {
                StringBuilder data = new StringBuilder();
                data.append("login="+login);
                data.append("&password="+password);
                data.append("&id="+id);
                json_objects = shop_c.get_products(data.toString());
                try {
                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ((Activity) getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    products_adapter = new products_adapter(json_objects);
                                    products_adapter.setHasStableIds(true);
                                    items.setNestedScrollingEnabled(false);
                                    items.setHasFixedSize(true);
                                    items.setItemViewCacheSize(3000);
                                    items.setAdapter(products_adapter);

                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }
                });
                }catch (Exception e){

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

    public Thread download_more(String id1){
        return new Thread() {

            @Override
            public void run() {
                StringBuilder data = new StringBuilder();
                data.append("login="+login);
                data.append("&password="+password);
                data.append("&id="+id);
                data.append("&id1="+id1);
                List<JSONObject> list1 = shop_c.get_more_products(data.toString());
                json_objects.addAll(list1);
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
                for (JSONObject json_object: list1) {
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
