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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.R;
import com.books1.connections.main.profile_c;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class my_notifications extends BottomSheetDialogFragment {
    String login, password, id = "";

    SharedPreferences sharedPreferences;

    List<JSONObject> array = new ArrayList<>();

    RecyclerView notifications;
    notifications_adapter notifications_adapter;
    Thread get_notifications;

    my_not_in my_not_in;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notifications, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sharedPreferences.getString("id","");
        login = sharedPreferences.getString("login","");
        password = sharedPreferences.getString("password","");

        notifications = v.findViewById(R.id.my_shop);
        notifications_adapter = new notifications_adapter(array);
        notifications.setLayoutManager(new LinearLayoutManager(getContext()));
        notifications.setAdapter(notifications_adapter);
        get_notifications = new Thread(){
            @Override
            public void run() {
                super.run();
                StringBuilder data = new StringBuilder();
                data.append("login="+login);
                data.append("&password="+password);
                data.append("&id="+id);
                profile_c profile_c = new profile_c();
                array = profile_c.get_notifications(data.toString());
                try {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifications_adapter = new notifications_adapter(array);
                            notifications.setLayoutManager(new LinearLayoutManager(getContext()));
                            notifications.setAdapter(notifications_adapter);
                        }
                    });
                }catch (Exception e){

                }
            }
        };
        get_notifications.start();
        return v;
    }

    public class notifications_adapter extends RecyclerView.Adapter<notifications_adapter.ViewHolder>{

        List<JSONObject> array1;

        public notifications_adapter(List<JSONObject> array){
            this.array1 = array;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            Button accept;
            Button cancel;
            TextView name;
            ConstraintLayout notification;
            ImageView see;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }


        @NonNull
        @Override
        public notifications_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.notification, parent, false);
            notifications_adapter.ViewHolder viewHolder = new notifications_adapter.ViewHolder(view);
            viewHolder.accept = view.findViewById(R.id.accept);
            viewHolder.cancel = view.findViewById(R.id.cancel);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.notification = view.findViewById(R.id.notification);
            viewHolder.see = view.findViewById(R.id.see);


            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull notifications_adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {
                holder.name.setText(array.get(position).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.accept.setOnClickListener(new View.OnClickListener() {
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
                                data.append("&id1=" + array1.get(position).getString("res_id"));
                                data.append("&id3=" + array1.get(position).getString("id"));
                                data.append("&pages=" + array1.get(position).getString("pages"));
                                data.append("&size=" + array1.get(position).getString("size"));
                                profile_c profile_c = new profile_c();
                                String message = profile_c.accept_sold(data.toString());
                                ((Activity)getActivity()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(!message.equals("er")){
                                            sold_bottom sold_bottom = new sold_bottom();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("nups",message);
                                            sold_bottom.setArguments(bundle);
                                            sold_bottom.show(getChildFragmentManager(), "TAG");
                                            array.remove(position);
                                            notifyItemRemoved(position);
                                        }
                                    }
                                });
                            }catch (Exception e){

                            }
                        }
                    };
                    thread.start();
                }
            });

            holder.cancel.setOnClickListener(new View.OnClickListener() {

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
                                data.append("&id1=" + array1.get(position).getString("res_id"));
                                data.append("&id3=" + array1.get(position).getString("id"));
                                data.append("&pages=" + array1.get(position).getString("pages"));
                                data.append("&size=" + array1.get(position).getString("size"));
                                profile_c profile_c = new profile_c();
                                String message = profile_c.cancel_sold(data.toString());
                                ((Activity)getActivity()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                            array.remove(position);
                                            notifyItemRemoved(position);

                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    };

                    thread.start();
                }
            });

            holder.notification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        my_not_in.go_to_profile_1(array1.get(position).getString("res_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.see.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        my_not_in.show_book_1(array1.get(position).getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return array1.size();
        }


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        my_not_in = (my_not_in) context;
    }

    public interface my_not_in{
        public void go_to_profile_1(String id);
        public String show_book_1(String book_id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (get_notifications.isAlive()){
            get_notifications.interrupt();
        }
    }
}
