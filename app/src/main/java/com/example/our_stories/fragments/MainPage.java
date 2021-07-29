package com.example.our_stories.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.our_stories.R;
import com.google.android.material.appbar.AppBarLayout;

public class MainPage extends Fragment {

    private MainPageViewModel mViewModel;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;

    public static MainPage newInstance() {
        return new MainPage();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
//        appBarLayout = getActivity().findViewById(R.id.app_bar_main);
//        appBarLayout.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainPageViewModel.class);
        // TODO: Use the ViewModel
    }

}