package com.example.gkude;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
import com.example.gkude.adapter.EntityPropertyAdapter;
import com.example.gkude.adapter.EntityRelationAdapter;
import com.example.gkude.adapter.ProblemAdapter;
import com.example.gkude.bean.EntityBean;
import com.example.gkude.bean.ProblemBean;
import com.example.gkude.bean.PropertyBean;
import com.example.gkude.bean.RelationBean;
import com.example.gkude.ui.chat.Message;

import org.javatuples.Triplet;
import org.w3c.dom.Entity;

import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
        EditText user_answer = findViewById(R.id.user_answer);
        Button sumit_answer = findViewById(R.id.submit_answer);
        TextView answer_title = findViewById(R.id.answer_title);
        TextView problem_answer = findViewById(R.id.problem_answer);
        answer_title.setVisibility(View.INVISIBLE);
        problem_answer.setVisibility(View.INVISIBLE);
        // 处理字符串
        int A = qBody.indexOf("A.");
        int B = qBody.indexOf("B.");
        int C = qBody.indexOf("C.");
        int D = qBody.indexOf("D.");
        String tmp = qBody.substring(0, A) + "\n" + qBody.substring(A,B) + "\n" + qBody.substring(B,C) + '\n' + qBody.substring(C,D) + "\n" + qBody.substring(D);
        problem.setText(tmp);

        sumit_answer.setOnClickListener(view -> {
            String content = user_answer.getText().toString();
            System.out.println("user sumit answer:" + content);
            answer_title.setVisibility(View.VISIBLE);
            problem_answer.setVisibility(View.VISIBLE);
            problem_answer.setText(qAnswer);
        });

    }

}
