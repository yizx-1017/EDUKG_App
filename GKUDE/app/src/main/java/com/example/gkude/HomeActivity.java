package com.example.gkude;

import android.content.Intent;
import android.graphics.Typeface;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout. content_main);
        tabViewModel = new ViewModelProvider(this).get(TabViewModel.class);
        initNavView();
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
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("HomeActivity", "message back");
        List<String> cat = data.getStringArrayListExtra("category");
        tabViewModel = new ViewModelProvider(this).get(TabViewModel.class);
        tabViewModel.setCategory(data.getStringArrayListExtra("category"));
        tabViewModel.setDelCategory(data.getStringArrayListExtra("delCategory"));
        for (String s: cat){
            Log.e("HomeActivity", s);
        }
    }
}