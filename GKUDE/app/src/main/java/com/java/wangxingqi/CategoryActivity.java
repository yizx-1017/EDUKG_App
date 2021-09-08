package com.java.wangxingqi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.java.wangxingqi.ui.home.DragGridLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryActivity extends AppCompatActivity {
    private DragGridLayout gridLayout1;
    private DragGridLayout gridLayout2;
    private List<String> mCategory;
    private List<String> mDelCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Data passed from previous activity
        Intent intent = getIntent();
        mCategory = (ArrayList<String>) intent.getSerializableExtra("category");
        mDelCategory = (ArrayList<String>) intent.getSerializableExtra("delCategory");

        // Init view via data
        initGridLayout();
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.entity_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initGridLayout() {
        // init view
        gridLayout1 = findViewById(R.id.arr_grid1);
        gridLayout1.setIsRemain(true);
        gridLayout2 = findViewById(R.id.arr_grid2);
        gridLayout1.setCanDrag(true);

        ArrayList<String> category = new ArrayList<>(mCategory);
        ArrayList<String> delCategory = new ArrayList<>(mDelCategory);
        gridLayout1.setItems(category);
        gridLayout2.setItems(delCategory);

        gridLayout1.setOnDragItemClickListener(new DragGridLayout.OnDragItemClickListener() {
            @Override
            public void onDragItemClick(TextView tv) {
                Log.e("CategoryActivity", tv.getText().toString());
                mCategory.remove(tv.getText().toString().replace("+", ""));
                mDelCategory.add(tv.getText().toString().replace("+", ""));
                gridLayout1.removeView(tv);
                gridLayout2.addGridItem(tv.getText().toString());
            }

            @Override
            public void onDrag(List<String> s) {
                mCategory = s;
                mDelCategory = gridLayout2.items;
            }
        });

        gridLayout2.setOnDragItemClickListener(new DragGridLayout.OnDragItemClickListener() {
            @Override
            public void onDragItemClick(TextView tv) {
                mDelCategory.remove(tv.getText().toString().replace("+", ""));
                mCategory.add(tv.getText().toString().replace("+", ""));
                gridLayout2.removeView(tv);
                gridLayout1.addGridItem(tv.getText().toString());
            }

            @Override
            public void onDrag(List<String> s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mCategory.size() == 0){
            Toast.makeText(getApplicationContext(), "必须至少选择一个频道", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = getIntent();
        intent.putExtra("category", (Serializable) mCategory);
        intent.putExtra("delCategory", (Serializable) mDelCategory);
        for(String s: mCategory){
            Log.e("CategoryActivity", s);
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}
