package com.example.smartparkingsystem;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ActivityAdapter extends FragmentStateAdapter {


    public ActivityAdapter(@NonNull FragmentHome fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new CurrentFragment();

            case 1:
                return new UpcomingFragment();
            case 2:
                return new HistoryFragment();
            default:
                return new CurrentFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
