package com.java.wangxingqi;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
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
        setRegion(problemNumText.getEditText());
        Button click = findViewById(R.id.shuffle_button);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String problemValue = problemNumText.getEditText().getText().toString();
                if (!problemValue.isEmpty()) {
                    problemList = userRepository.getProblemRecommendation(Integer.parseInt(problemValue));
                    setRecyclerView();
                    if (problemList.isEmpty()) {
                        Toast.makeText(ProblemRecommendationActivity.this, "暂时没有题库哦，多做做题再来看", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProblemRecommendationActivity.this, "不能输入为空哈", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int MIN_MARK = 0;
    private int MAX_MARK = 100;
    //private void setRegion(EditText et)
    private void setRegion( final EditText et)
    {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 1)
                {
                    if (MIN_MARK != -1 && MAX_MARK != -1)
                    {
                        int num = Integer.parseInt(s.toString());
                        if (num > MAX_MARK)
                        {
                            s = String.valueOf(MAX_MARK);
                            et.setText(s);
                        }
                        else if(num < MIN_MARK)
                            s = String.valueOf(MIN_MARK);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s != null && !s.equals(""))
                {
                    if (MIN_MARK != -1 && MAX_MARK != -1)
                    {
                        int markVal = 0;
                        try
                        {
                            markVal = Integer.parseInt(s.toString());
                        }
                        catch (NumberFormatException e)
                        {
                            markVal = 0;
                        }
                        if (markVal > MAX_MARK)
                        {
                            Toast.makeText(getBaseContext(), "做题不能贪多啊，最多一百道", Toast.LENGTH_SHORT).show();
                            et.setText(String.valueOf(MAX_MARK));
                        }
                        return;
                    }
                }
            }
        });
    }
}