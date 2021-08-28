package com.example.gkude;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.gkude.bean.EntityBean;
import com.example.gkude.bean.ProblemBean;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

//import com.example.gkude.databinding.ActivityHomeBinding;
import com.example.gkude.ui.home.TabViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.orm.SugarContext;

public class HomeActivity extends AppCompatActivity {
    public static final int SUBJECTS = 111, CATEGORY = 13;
    private TabViewModel tabViewModel;
//    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        binding = ActivityHomeBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        setContentView(R.layout.activity_main);

        tabViewModel = new ViewModelProvider(this).get(TabViewModel.class);
        initNavView();
//        initSearchbar();
    }

    private void initNavView() {
        System.out.println("init NavView");
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("MainActivity", ""+requestCode);
        switch (requestCode){
            case SUBJECTS:{
                Log.e("MainActivity", "news back");
                // refresh
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                overridePendingTransition(0, 0);
                break;
            }
            case CATEGORY:{
                Log.e("MainActivity", "cat back");
                List<String> cat = data.getStringArrayListExtra("category");
                tabViewModel = new ViewModelProvider(this).get(TabViewModel.class);
                tabViewModel.setCategory(data.getStringArrayListExtra("category"));
                tabViewModel.setDelCategory(data.getStringArrayListExtra("delCategory"));
                for (String s: cat){
                    Log.e("MainActivity", s);
                }
                break;
            }
            default:
                Log.e("MainActivity", "default");
        }
    }
    private void initSearchbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        final SearchView mSearchView = findViewById(R.id.search_view);
        mSearchView.findViewById(R.id.search_plate).setBackground(null);
        mSearchView.findViewById(R.id.submit_area).setBackground(null);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // submit query text & go to next page
                Log.e("HomeActivity", "TextSubmit : " + s);
                mSearchView.setIconified(true);
                Intent intent = new Intent(HomeActivity.this, EntitySearchedActivity.class);
                intent.putExtra("keyword", s);
                startActivityForResult(intent, SUBJECTS);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

}