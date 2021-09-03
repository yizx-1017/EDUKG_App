package com.example.gkude;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
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
        setContentView(R.layout.activity_problem_view);
        problem_id = getIntent().getLongExtra("problem_id", -1);
        ProblemBean problemBean = ProblemBean.findById(ProblemBean.class, problem_id);
        qBody = problemBean.getQBody();
        qAnswer = problemBean.getQAnswer();
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

    private void initView() {

//        TextView mLabel = findViewById(R.id.qBody);
//        TextView mInfo = findViewById(R.id.qAnswer);
//        mLabel.setText(qBody);
//        mInfo.setText(qAnswer);

    }

}
