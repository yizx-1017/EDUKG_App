package com.java.wangxingqi.ui.home;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;

import com.java.wangxingqi.CategoryActivity;
import com.java.wangxingqi.EntitySearchedActivity;
import com.java.wangxingqi.R;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.java.wangxingqi.HomeActivity;
import com.java.wangxingqi.adapter.HomePagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private TabViewModel tabViewModel;
    private HomePagerAdapter homePagerAdapter;
    private Spinner spinner_filter, spinner_sorter;
    private String course, sort;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private void initView(View view){
        // init Tab view
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

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
//                viewPager.setCurrentItem(0);
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
        spinner_filter = view.findViewById(R.id.spin_filter);
        spinner_sorter = view.findViewById(R.id.spin_sorter);
        ArrayAdapter<CharSequence> spinnerFilterAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.course, R.layout.my_support_simple_spinner_dropdown_item);
        spinner_filter.setAdapter(spinnerFilterAdapter);
        spinner_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String courseCN = adapterView.getItemAtPosition(position).toString();
//                Toast.makeText(getContext(), "选择的学科是：" + courseCN, Toast.LENGTH_SHORT).show();
                switch (courseCN){
                    case "语文":
                        course = "chinese";
                        break;
                    case "数学":
                        course = "math";
                        break;
                    case "英语":
                        course = "english";
                        break;
                    case "物理":
                        course = "physics";
                        break;
                    case "化学":
                        course = "chemistry";
                        break;
                    case "生物":
                        course = "biology";
                        break;
                    case "历史":
                        course = "history";
                        break;
                    case "政治":
                        course = "politics";
                        break;
                    case "地理":
                        course = "geo";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> spinnerSorterAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.sorters, R.layout.my_support_simple_spinner_dropdown_item);
        spinner_sorter.setAdapter(spinnerSorterAdapter);
        spinner_sorter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String courseCN = adapterView.getItemAtPosition(position).toString();
//                Toast.makeText(getContext(), "选择的排序是：" + courseCN, Toast.LENGTH_SHORT).show();
                switch (courseCN){
                    case "默认":
                        sort = "normal";
                        break;
                    case "字母序":
                        sort = "abc";
                        break;
                    case "长度":
                        sort = "length";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final SearchView mSearchView = view.findViewById(R.id.search_view);
//        mSearchView.findViewById(R.id.search_plate).setBackground(null);
//        mSearchView.findViewById(R.id.submit_area).setBackground(null);
        // TODO(zhiyuxie): check these two lines
//        spinner_filter.setBackground(null);
//        spinner_sorter.setBackground(null);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // submit query text & go to next page
                Log.e("HomeActivity", "TextSubmit : " + s);
                mSearchView.setIconified(true);
                Intent intent = new Intent(getActivity(), EntitySearchedActivity.class);
                intent.putExtra("keyword", s);
                System.out.println("in HomeFragment, course:"+course);
                intent.putExtra("course", course);
                intent.putExtra("sort", sort);

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
