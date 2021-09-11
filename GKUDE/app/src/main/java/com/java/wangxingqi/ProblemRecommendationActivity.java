package com.java.wangxingqi;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.java.wangxingqi.adapter.ProblemAdapter;
import com.java.wangxingqi.bean.ProblemBean;
import com.java.wangxingqi.server.UserDataSource;
import com.java.wangxingqi.server.UserRepository;

import java.util.List;
import java.util.Objects;

public class ProblemRecommendationActivity extends AppCompatActivity {

    private List<ProblemBean> problemList;
    private ProblemAdapter mAdapter;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_recommendation);
        userRepository = UserRepository.getInstance(new UserDataSource());
        initToolbar();
        initInput();
    }


    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.entity_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle("试题推荐");
    }

    private void setRecyclerView() {
        // Set layout manager
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Set adapter for recyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ProblemAdapter(problemList);
        recyclerView.setAdapter(mAdapter);
    }

    private void initInput() {
        TextInputLayout problemNumText = findViewById(R.id.problem_number);

        Button click = findViewById(R.id.shuffle_button);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String problemValue = problemNumText.getEditText().getText().toString();
                if (!problemValue.isEmpty()) {
                    problemList = userRepository.getProblemRecommendation(Integer.parseInt(problemValue));
                    setRecyclerView();
                } else {
                    Toast.makeText(ProblemRecommendationActivity.this, "暂时没有题库哦，多做做题再来看", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}