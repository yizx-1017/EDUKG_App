package com.example.gkude;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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

public class EntityViewActivity extends AppCompatActivity {
    private Long entity_id;
    private String label, course, uri, category;
    private EntityRelationAdapter relation_adapter;
    private EntityPropertyAdapter property_adapter;
    private ProblemAdapter problem_adpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_view);
        entity_id = getIntent().getLongExtra("entity_id", -1);
        if(entity_id == -1) {
            label = getIntent().getStringExtra("entity_label");
            course = getIntent().getStringExtra("entity_course");
            uri = getIntent().getStringExtra("entity_uri");
        }
        initObserver();
        initToolbar();
    }

    private void initObserver() {
        Observer<EntityBean> observer = new Observer<EntityBean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(EntityBean entityBean) {
                Log.i("EntityBean", String.valueOf(entityBean.isVisited()));
                System.out.println("in. entityView observer onNext");
                label = entityBean.getLabel();
                category = entityBean.getCategory();
                List<PropertyBean> properties = entityBean.getPropertiesFromStore();
                properties.removeIf(p->p.getObject().contains("http://"));
                System.out.println("onNext!!!!! "+entityBean.getCourse());

                relation_adapter = new EntityRelationAdapter(entityBean.getRelationsFromStore(), entityBean.getCourse());
                property_adapter = new EntityPropertyAdapter(properties);
                problem_adpater = new ProblemAdapter(entityBean.getProblemsFromStore());
                System.out.println("onNext!" + entityBean.getProblems());
                entityBean.save();
                initView();
                initRecyclerView();
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
        System.out.println("in EntityViewActivity.java, observer, prepare to getEntityInfo");
        if(entity_id != -1)
            Manager.getEntityInfo(EntityBean.findById(EntityBean.class, entity_id), observer);
        else {
            EntityBean tmp_bean = new EntityBean();
            tmp_bean.setUri(uri);
            tmp_bean.setLabel(label);
            tmp_bean.setCourse(course);
            Manager.getEntityInfo(tmp_bean, observer);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.entity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("实体信息");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntityViewActivity.this.finish();
            }
        });
    }

    private void initRecyclerView() {

        // Set adapter for recyclerView
        RecyclerView relation = findViewById(R.id.recycler_view_relation);
        relation.setAdapter(relation_adapter);
        RecyclerView property = findViewById(R.id.recycler_view_property);
        property.setAdapter(property_adapter);
        RecyclerView problem = findViewById(R.id.recycler_view_problem);
        problem.setAdapter(problem_adpater);
    }

    private void initView() {

        TextView mLabel = findViewById(R.id.entity_label);
        TextView mInfo = findViewById(R.id.entity_description);

        System.out.println("initView!" + label);
        mLabel.setText(label);
        mInfo.setText(category);

    }

}
