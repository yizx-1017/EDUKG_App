package com.java.wangxingqi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

//import com.example.gkude.databinding.ActivityHomeBinding;
import com.java.wangxingqi.adapter.HomePagerAdapter;
import com.java.wangxingqi.ui.home.TabViewModel;

import java.util.List;

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
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        homePagerAdapter.setCategory(data.getStringArrayListExtra("category"));
        homePagerAdapter.setDelCategory(data.getStringArrayListExtra("delCategory"));
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(homePagerAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}