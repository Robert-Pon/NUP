package com.books1.books.main.fragments.profile.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.R;
import com.books1.connections.main.profile_c;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class my_shopping extends BottomSheetDialogFragment {
    String login, password, id = "";

    SharedPreferences sharedPreferences;

    List<JSONObject> array = new ArrayList<>();

    RecyclerView my_shop;
    my_shop_adapter my_shop_adapter;
    my_shop_in my_shop_in;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_shop, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sharedPreferences.getString("id","");
        login = sharedPreferences.getString("login","");
        password = sharedPreferences.getString("password","");

        my_shop = v.findViewById(R.id.my_shop);

        my_shop_adapter = new my_shop_adapter(array);
        my_shop.setLayoutManager(new LinearLayoutManager(getContext()));
        my_shop.setAdapter(my_shop_adapter);


        Thread get_notifications = new Thread(){
            @Override
            public void run() {
                super.run();
                StringBuilder data = new StringBuilder();
                data.append("login="+login);
                data.append("&password="+password);
                data.append("&id="+id);
                profile_c profile_c = new profile_c();
                array = profile_c.get_my_shop(data.toString());
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        my_shop_adapter = new my_shop_adapter(array);
                        my_shop.setLayoutManager(new LinearLayoutManager(getContext()));
                        my_shop.setAdapter(my_shop_adapter);

                    }
                });
            }
        };
        get_notifications.start();
        return v;
    }

    public class my_shop_adapter extends RecyclerView.Adapter {

        List<JSONObject> array;

        public my_shop_adapter(List<JSONObject> array){
            this.array = array;
        }

        public class ViewHolder1 extends RecyclerView.ViewHolder {
            Button cancel;
            TextView name;
            ImageView see;
            public ViewHolder1(@NonNull View itemView) {
                super(itemView);
            }
        }

         public class ViewHolder2 extends RecyclerView.ViewHolder {
            TextView cancel;
            TextView name;
            ImageView see;
            public ViewHolder2(@NonNull View itemView) {
                super(itemView);
            }
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType){
                case 0:
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.my_bought, parent, false);
                    my_shop_adapter.ViewHolder1 viewHolder = new my_shop_adapter.ViewHolder1(view);
                    viewHolder.cancel = view.findViewById(R.id.cancel);
                    viewHolder.name = view.findViewById(R.id.name);
                    viewHolder.see = view.findViewById(R.id.see);
                    return viewHolder;
                case 1:
                    View view1 = LayoutInflater.from(getContext()).inflate(R.layout.my_bought_no, parent, false);
                    my_shop_adapter.ViewHolder2 viewHolder1 = new my_shop_adapter.ViewHolder2(view1);
                    viewHolder1.cancel = view1.findViewById(R.id.cancel);
                    viewHolder1.name = view1.findViewById(R.id.name);
                    viewHolder1.see = view1.findViewById(R.id.see);
                    return viewHolder1;
            }
            return null;

        }


        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            try {
                switch (array.get(position).getString("buyType")) {
                    case "3":
                        try {
                            ((ViewHolder2)holder).name.setText(array.get(position).getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ViewHolder2)holder).cancel.setText("Zakup zaakceptowany");
                        ((ViewHolder2)holder).cancel.setTextColor(getResources().getColor(R.color.white));
                        ((ViewHolder2)holder).see.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    my_shop_in.show_book_1(array.get(position).getString("id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    break;
                    case "1":
                        try {
                            ((ViewHolder1)holder).name.setText(array.get(position).getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ViewHolder1)holder).cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Thread thread = new Thread(){
                                    @Override
                                    public void run() {
                                        super.run();
                                        try {
                                            StringBuilder data = new StringBuilder();
                                            data.append("login=" + login);
                                            data.append("&password=" + password);
                                            data.append("&id=" + id);
                                            data.append("&id1=" + id);
                                            data.append("&id3=" + array.get(position).getString("id"));
                                            data.append("&pages=" + array.get(position).getString("pages"));
                                            data.append("&size=" + array.get(position).getString("size"));
                                            profile_c profile_c = new profile_c();
                                            String message = profile_c.cancel_sold(data.toString());
                                            if(!message.equals("er")){
                                                ((Activity) getContext()).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            array.get(position).put("buyType", "0");
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        notifyDataSetChanged();

                                                    }
                                                });
                                            }
                                        }catch (Exception e){

                                        }
                                    }
                                };
                                thread.start();
                            }
                        });

                        ((ViewHolder1)holder).see.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    my_shop_in.show_book_1(array.get(position).getString("id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    break;
                    case "0":
                        try {
                            ((ViewHolder2)holder).name.setText(array.get(position).getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ViewHolder2)holder).cancel.setText("Zakup anulowany");
                        ((ViewHolder2)holder).cancel.setTextColor(getResources().getColor(R.color.red));
                        ((ViewHolder2)holder).see.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    my_shop_in.show_book_1(array.get(position).getString("id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                     case "4":
                        try {
                            ((ViewHolder2)holder).name.setText(array.get(position).getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ViewHolder2)holder).cancel.setText("Zakup anulowany");
                         ((ViewHolder2)holder).cancel.setTextColor(getResources().getColor(R.color.red));
                         ((ViewHolder2)holder).see.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 try {
                                     my_shop_in.show_book_1(array.get(position).getString("id"));
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
                             }
                         });
                        break;

                }
            }catch (Exception e){

            }




        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public int getItemViewType(int position) {
            int o = 1;
            try {
                if(array.get(position).getString("buyType").toString().equals("1")){
                    o = 0;
                }
                return o;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return o;
        }
        @Override
        public int getItemCount() {
            return array.size();
        }


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        my_shop_in = (my_shop_in) context;
    }

    public interface my_shop_in{
        public String show_book_1(String book_id);
    }
}
