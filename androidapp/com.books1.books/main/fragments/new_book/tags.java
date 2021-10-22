package com.books1.books.main.fragments.new_book;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.R;
import com.books1.connections.main.add_book_c;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class tags extends BottomSheetDialogFragment {
    String id, login, password = "";

    RecyclerView tags;
    RecyclerView my_tags;
    EditText my_tag;

    Thread search_tag = new Thread();

    SharedPreferences sharedPreferences;

    List<JSONObject> tags_list = new ArrayList<>();
    List<JSONObject> my_tags_list = new ArrayList<>();
    List<String> my_tags_list_ids = new ArrayList<>();

    tags_adapter tags_adapter;
    my_tags_adapter my_tags_adapter;

    Button add_tag;

    tags_in tags_in;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tags, container, false);

        tags = v.findViewById(R.id.tags);
        my_tags = v.findViewById(R.id.my_tags);
        my_tag = v.findViewById(R.id.my_tag);
        add_tag = v.findViewById(R.id.add_tag);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sharedPreferences.getString("id","");
        login = sharedPreferences.getString("login","");
        password = sharedPreferences.getString("password","");

        tags_adapter = new tags_adapter(tags_list);
        tags.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        tags.setAdapter(tags_adapter);

        my_tags_adapter = new my_tags_adapter(my_tags_list);
        my_tags.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        my_tags.setAdapter(my_tags_adapter);

        my_tag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(search_tag.isAlive()){
                    search_tag.interrupt();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                search_tag = get_tags(my_tag.getText().toString());
                search_tag.start();
            }
        });

        add_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Thread create = create_tag(my_tag.getText().toString());
                    create.start();

            }
        });
        return v;
    }

    public class tags_adapter extends RecyclerView.Adapter<tags_adapter.ViewHolder>{
        List<JSONObject> tags;

        public tags_adapter(List<JSONObject> tags){
            this.tags = tags;
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tag;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

        @NonNull
        @Override
        public tags_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.tag, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            viewHolder.tag = v.findViewById(R.id.name);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull tags_adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {
                holder.tag.setText(tags_list.get(position).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            holder.tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    my_tags_list.add(tags_list.get(position));
                    try {
                        my_tags_list_ids.add(tags_list.get(position).getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    my_tags_adapter.notifyDataSetChanged();
                    tags_in.list_setter(my_tags_list_ids);
                }
            });
        }

        @Override
        public int getItemCount() {
            return tags_list.size();
        }


    }

    public class my_tags_adapter extends RecyclerView.Adapter<my_tags_adapter.ViewHolder>{
        List<JSONObject> tags;

        public my_tags_adapter(List<JSONObject> tags){
            this.tags = tags;
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tag;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

        @NonNull
        @Override
        public my_tags_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.tag, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            viewHolder.tag = v.findViewById(R.id.name);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull my_tags_adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {
                holder.tag.setText(my_tags_list.get(position).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            holder.tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    my_tags_list.remove(position);
                    my_tags_adapter.notifyItemRemoved(position);
                    my_tags_list_ids.remove(position);
                    tags_in.list_setter(my_tags_list_ids);
                }
            });
        }

        @Override
        public int getItemCount() {
            return my_tags_list.size();
        }


    }




    public Thread get_tags(String tag){
        return new Thread(){
            @Override
            public void run() {
                super.run();
                StringBuilder data = new StringBuilder();
                data.append("login="+login);
                data.append("&password="+password);
                data.append("&id="+id);
                data.append("&search="+tag);
                add_book_c add_book_c = new add_book_c();
                List<JSONObject> tags = add_book_c.search_tag(data.toString());
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tags_list = tags;
                        tags_adapter.notifyDataSetChanged();
                    }
                });

            }
        };
    }

    public Thread create_tag(String tag){
        return new Thread(){
            @Override
            public void run() {
                super.run();
                StringBuilder data = new StringBuilder();
                data.append("login="+login);
                data.append("&password="+password);
                data.append("&id="+id);
                data.append("&name="+tag);
                add_book_c add_book_c = new add_book_c();
                JSONObject tags = add_book_c.create_tag(data.toString());
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        my_tags_list.add(tags);
                        my_tags_adapter.notifyDataSetChanged();
                        my_tag.setText("");
                    }
                });

            }
        };
    }
    public interface tags_in{
        public void list_setter (List<String> ids);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        tags_in = (tags_in) context;
    }
}
