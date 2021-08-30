package com.example.gkude.ui.home;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;

import com.example.gkude.CategoryActivity;
import com.example.gkude.EntitySearchedActivity;
import com.example.gkude.R;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
import com.example.gkude.HomeActivity;
import com.example.gkude.adapter.HomePagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private TabViewModel tabViewModel;
    private HomePagerAdapter homePagerAdapter;
    private Spinner spinner_filter;
    private List<String> data_list;
    private ArrayAdapter<String> arrayAdapter;

    private void initView(View view){
        // init Tab view
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.view_pager);

        // bind: fragment -> viewPager -> tabLayout
        homePagerAdapter = new HomePagerAdapter(getParentFragmentManager());
        viewPager.setAdapter(homePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // init drag layout
        ImageView addTab = view.findViewById(R.id.ic_add);
        addTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("click the category button");
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                intent.putExtra("category", new ArrayList<>(Objects.requireNonNull(tabViewModel.getCategory().getValue())));
                intent.putExtra("delCategory",  new ArrayList<>(Objects.requireNonNull(tabViewModel.getDelCategory().getValue())));
                Log.e("HomeFragment", String.valueOf(HomeActivity.CATEGORY));
                startActivityForResult(intent, HomeActivity.CATEGORY);
            }
        });
        //init Search Bar
        initSearchbar(view);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initView(root);
        tabViewModel = new ViewModelProvider(getActivity()).get(TabViewModel.class);

        tabViewModel.getCategory().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> s) {
                homePagerAdapter.setCategory(s);
                Log.e("HomeFragment", s.toString());
            }
        });

        tabViewModel.getDelCategory().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> s) {
                homePagerAdapter.setDelCategory(s);
                Log.e("HomeFragment", s.toString());
            }
        });
        return root;
    }

    private void initSearchbar(View view) {
        Toolbar mToolbar = view.findViewById(R.id.toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        spinner_filter = view.findViewById(R.id.spin_filter);
        data_list = new ArrayList<String>();
        data_list.add("filter 1");
        data_list.add("filter 2");
        data_list.add("filter 3");
        arrayAdapter = new ArrayAdapter<String>(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item, data_list);
        spinner_filter.setAdapter(arrayAdapter);

        final SearchView mSearchView = view.findViewById(R.id.search_view);
        System.out.println("ccccc");
        mSearchView.findViewById(R.id.search_plate).setBackground(null);
        mSearchView.findViewById(R.id.submit_area).setBackground(null);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // submit query text & go to next page
                Log.e("HomeActivity", "TextSubmit : " + s);
                mSearchView.setIconified(true);
                Intent intent = new Intent(getActivity(), EntitySearchedActivity.class);
                intent.putExtra("keyword", s);
//                Spinner spinner_filter = view.findViewById(R.id.spin_filter);

                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }
}
