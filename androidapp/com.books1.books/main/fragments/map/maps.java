package com.books1.books.main.fragments.map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.books1.connections.main.map_c;
import com.example.books.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class maps extends Fragment {
    EditText search;
    ImageView search_b;
    String id, login, password = "";
    SharedPreferences sharedPreferences;
    List<JSONObject> json_objects;
    com.books1.books.main.fragments.map.products_from_map products_from_map = new products_from_map();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.maps_layout, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapV);
        search = v.findViewById(R.id.search);
        search_b = v.findViewById(R.id.search_b);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        id = sharedPreferences.getString("id","");
        login = sharedPreferences.getString("login","");
        password = sharedPreferences.getString("password","");
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {


                search_b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        googleMap.clear();
                        StringBuilder data = new StringBuilder();
                        List<LatLng> lat_lngs = new ArrayList<>();
                        data.append("login="+login);
                        data.append("&password="+password);
                        data.append("&id="+id);
                        data.append("&search="+search.getText().toString());
                        data.append("&x="+googleMap.getProjection().getVisibleRegion().latLngBounds.northeast.latitude);
                        data.append("&x1="+googleMap.getProjection().getVisibleRegion().latLngBounds.southwest.latitude);
                        data.append("&y="+googleMap.getProjection().getVisibleRegion().latLngBounds.northeast.longitude);
                        data.append("&y1="+googleMap.getProjection().getVisibleRegion().latLngBounds.southwest.longitude);
                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                json_objects = new ArrayList<>();
                                map_c map_c = new map_c();
                                json_objects = map_c.get_map(data.toString());
                                ((Activity)getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (JSONObject js : json_objects){
                                            try {
                                                LatLng latLng = new LatLng(Double.parseDouble(js.getString("x")),Double.parseDouble(js.getString("y")));
                                                lat_lngs.add(latLng);
                                                googleMap.addMarker(new MarkerOptions().position(latLng));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }


                                    }
                                });
                            }
                        };
                        thread.start();


                    }
                });
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        Bundle data = new Bundle();
                        data.putDouble("x", marker.getPosition().latitude);
                        data.putDouble("y", marker.getPosition().longitude);
                        data.putString("search", String.valueOf(search.getText()));
                        products_from_map.setArguments(data);
                        products_from_map.show(getChildFragmentManager(), "D");
                        return false;
                    }
                });
            }
        });


        return v;
    }
}
