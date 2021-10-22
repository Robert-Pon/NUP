package com.books1.books.main.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.books.R;
import com.books1.books.main.fragments.profile.settings.settings_fragment_manager;

public class view_pager extends Fragment {
    com.books1.books.main.fragments.profile.settings.settings_fragment_manager settings_fragment_manager = new settings_fragment_manager();
    com.books1.books.main.fragments.profile.profile profile = new profile();
    Fragment fragment = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_pager, container, false);
        ViewPager2 viewPager2 = v.findViewById(R.id.pager);
        pager pager = new pager(getActivity());
        viewPager2.setAdapter(pager);
        viewPager2.setCurrentItem(1);
        return v;
    }

    public class pager extends FragmentStateAdapter {


        public pager(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            switch (position){
                case 0:
                    fragment = settings_fragment_manager;
                    return fragment;
                case 1:
                    fragment = new profile();
                    return fragment;
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }


}
