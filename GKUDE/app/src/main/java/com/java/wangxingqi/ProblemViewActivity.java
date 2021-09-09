package com.java.wangxingqi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//import com.bumptech.glide.Glide;
import com.java.wangxingqi.bean.ProblemBean;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class ProblemViewActivity extends AppCompatActivity {
    private Long problem_id;
    private String qBody, qAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        problem_id = getIntent().getLongExtra("problem_id", -1);
        ProblemBean problemBean = ProblemBean.findById(ProblemBean.class, problem_id);
        qBody = problemBean.getQBody();
        qAnswer = problemBean.getQAnswer();
        System.out.println("in problem page: qbody" + qBody + "qAnswer:" + qAnswer);
        if(qAnswer.equals("A") || qAnswer.equals("B") || qAnswer.equals("C") || qAnswer.equals("D")) {
            setContentView(R.layout.activity_problem_choice_view);
            initChoiceView();
        }
        else {
            setContentView(R.layout.activity_problem_view);
            initNormalView();
        }
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.entity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("问题");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProblemViewActivity.this.finish();
            }
        });
        FloatingActionButton share = findViewById(R.id.fab_share_problem);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("problem share","onclick");
                Intent intent = new Intent(ProblemViewActivity.this, ShareActivity.class);
                intent.putExtra("isEntity", false);
                intent.putExtra("id", problem_id);
                startActivity(intent);
            }
        });
    }

    private void initNormalView() {
        TextView problem = findViewById(R.id.problem_text);
        EditText user_answer = findViewById(R.id.user_answer);
        Button sumit_answer = findViewById(R.id.submit_answer);
        TextView answer_title = findViewById(R.id.answer_title);
        TextView problem_answer = findViewById(R.id.problem_answer);
        answer_title.setVisibility(View.INVISIBLE);
        problem_answer.setVisibility(View.INVISIBLE);
        problem.setText(qBody);

        sumit_answer.setOnClickListener(view -> {
            String content = user_answer.getText().toString();
            System.out.println("user sumit answer:" + content);
            answer_title.setVisibility(View.VISIBLE);
            problem_answer.setVisibility(View.VISIBLE);
            problem_answer.setText(qAnswer);
        });
    }

    private void initChoiceView() {
        TextView problem = findViewById(R.id.problem_text);
        RadioButton radioButton1 = findViewById(R.id.radioButton1);
        RadioButton radioButton2 = findViewById(R.id.radioButton2);
        RadioButton radioButton3 = findViewById(R.id.radioButton3);
        RadioButton radioButton4 = findViewById(R.id.radioButton4);
        Button submit_answer = findViewById(R.id.submit_answer);
        TextView answer_title = findViewById(R.id.answer_title);
        TextView problem_answer = findViewById(R.id.problem_answer);
        answer_title.setVisibility(View.INVISIBLE);
        problem_answer.setVisibility(View.INVISIBLE);
        try {
            // 处理字符串
            int A = qBody.indexOf("A.");
            int B = qBody.indexOf("B.");
            int C = qBody.indexOf("C.");
            int D = qBody.indexOf("D.");
            String tmp = qBody.substring(0, A) + "\n" + qBody.substring(A, B) + "\n" + qBody.substring(B, C) + '\n' + qBody.substring(C, D) + "\n" + qBody.substring(D);
            problem.setText(qBody.substring(0, A));
            radioButton1.setText(qBody.substring(A, B));
            radioButton2.setText(qBody.substring(B, C));
            radioButton3.setText(qBody.substring(C, D));
            radioButton4.setText(qBody.substring(D));
        } catch(Exception e) {
            // if problem occurs, change to normal question view.
            setContentView(R.layout.activity_problem_view);
            initNormalView();
        };

        submit_answer.setOnClickListener(view -> {
            RadioGroup radioGroup = findViewById(R.id.radioGroup);
            int user_answer = radioGroup.getCheckedRadioButtonId();
            RadioButton user_selected = findViewById(user_answer);
            RadioButton right_button = findViewById(R.id.radioButton1);
            user_selected.setTextColor(getResources().getColor(R.color.wrong_choice));
            if(qAnswer.equals("A")) {
                right_button = findViewById(R.id.radioButton1);
            }
            else if(qAnswer.equals("B")) {
                right_button = findViewById(R.id.radioButton2);
            }
            else if(qAnswer.equals("C")) {
                right_button = findViewById(R.id.radioButton3);
            }
            else if(qAnswer.equals("D")) {
                right_button = findViewById(R.id.radioButton4);
            }
            right_button.setTextColor(getResources().getColor(R.color.right_choice));
            if(user_selected.getId() == right_button.getId()) {
                // 答对了
                Toast.makeText(ProblemViewActivity.this, "回答正确", Toast.LENGTH_SHORT).show();
            }
            else {
                // 答错了
                Toast.makeText(ProblemViewActivity.this, "回答错误", Toast.LENGTH_SHORT).show();
            }
            answer_title.setVisibility(View.VISIBLE);
            problem_answer.setVisibility(View.VISIBLE);
            problem_answer.setText(qAnswer);
            findViewById(R.id.radioButton1).setEnabled(false);
            findViewById(R.id.radioButton2).setEnabled(false);
            findViewById(R.id.radioButton3).setEnabled(false);
            findViewById(R.id.radioButton4).setEnabled(false);
        });

    }

}
