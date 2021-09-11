package com.java.wangxingqi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

//import com.bumptech.glide.Glide;
import com.java.wangxingqi.adapter.EntityPropertyAdapter;
import com.java.wangxingqi.adapter.EntityRelationAdapter;
import com.java.wangxingqi.adapter.ImageAdapter;
import com.java.wangxingqi.adapter.ProblemAdapter;
import com.java.wangxingqi.bean.EntityBean;
import com.java.wangxingqi.bean.ProblemBean;
import com.java.wangxingqi.bean.PropertyBean;
import com.java.wangxingqi.bean.RelationBean;
import com.java.wangxingqi.server.Result;
import com.java.wangxingqi.server.UserDataSource;
import com.java.wangxingqi.server.UserRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
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
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_view);
        userRepository = UserRepository.getInstance(new UserDataSource());
        entity_id = getIntent().getLongExtra("entity_id", -1);
        if(entity_id == -1) {
            label = getIntent().getStringExtra("entity_label");
            course = getIntent().getStringExtra("entity_course");
            category = getIntent().getStringExtra("entity_category");
            uri = getIntent().getStringExtra("entity_uri");
        }
        initObserver();
        initToolbar();
    }

    private void initObserver() {
        // 添加历史记录
        Observer<EntityBean> observer = new Observer<EntityBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull EntityBean entityBean) {
                System.out.println("in. entityView observer onNext");
                setUpEntityView(entityBean);
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
        System.out.println("in EntityViewActivity.java, observer, prepare to getEntityInfo");
        if(entity_id != -1) {
            EntityBean entityBean = EntityBean.findById(EntityBean.class, entity_id);
            if (entityBean == null) {
                this.finish();
            } else {
                setUpEntityView(entityBean);
            }
        }
        else {
            EntityBean tmp_bean = new EntityBean();
            tmp_bean.setUri(uri);
            tmp_bean.setLabel(label);
            tmp_bean.setCourse(course);
            tmp_bean.setCategory(category);
            Manager.getEntityInfo(tmp_bean, observer);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.entity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("实体信息");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> EntityViewActivity.this.finish());
        FloatingActionButton share = findViewById(R.id.fab_share);
        share.setOnClickListener(view -> {
            Intent intent = new Intent(EntityViewActivity.this, ShareActivity.class);
            intent.putExtra("isEntity", true);
            intent.putExtra("id", entity_id);
            startActivity(intent);
        });
        FloatingActionButton fav = findViewById(R.id.fab_fav);
        EntityBean bean = EntityBean.findById(EntityBean.class, entity_id);
        if (userRepository.getUser().getFavorites().contains(bean)) {
            fav.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        }
        fav.setOnClickListener(view -> {
            EntityBean fav_bean = EntityBean.findById(EntityBean.class, entity_id);
            if (!userRepository.getUser().getFavorites().contains(fav_bean)) {
                Log.i("fav button", "click to add favourite");
                Log.i("fav button", fav_bean.getCategory());
                Result<String> result = userRepository.addFavorite(fav_bean);
                fav.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                Log.i("fav button", result.getStatus().toString());
                if (result.getStatus().equals(200)){
                    Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_LONG).show();
                } else {
                    Log.i("fav button", result.getStatus().toString());
                    Toast.makeText(getApplicationContext(), "收藏成功，同步失败", Toast.LENGTH_LONG).show();
                }

            } else {
                Log.i("fav button", "click to cancel favourite");
                Result<String> result = userRepository.cancelFavorite(fav_bean);
                fav.setImageResource(android.R.drawable.star_big_off);
                if (result.getStatus().equals(200)) {
                    Toast.makeText(getApplicationContext(), "取消收藏", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "取消收藏，同步失败", Toast.LENGTH_LONG).show();
                }
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

        FloatingActionButton share = findViewById(R.id.fab_share);
        FloatingActionButton fav = findViewById(R.id.fab_fav);
        share.setVisibility(View.VISIBLE);
        fav.setVisibility(View.VISIBLE);

        TextView mLabel = findViewById(R.id.entity_label);
        TextView mInfo = findViewById(R.id.entity_description);

        System.out.println("initView!" + label);
        mLabel.setText(label);
        mInfo.setText(category);

    }

    private void setUpEntityView(EntityBean entityBean) {
        label = entityBean.getLabel();
        category = entityBean.getCategory();
        entity_id = entityBean.getId();
        List<PropertyBean> properties = entityBean.getPropertiesFromStore();
        List<RelationBean> relations = entityBean.getRelationsFromStore();
        List<ProblemBean> problems = entityBean.getProblemsFromStore();
        if (properties == null) {
            properties = new ArrayList<>();
        }
        if (relations == null) {
            relations = new ArrayList<>();
        }
        if (problems == null) {
            problems = new ArrayList<>();
        }
        System.out.println("onNext!!!!! " + entityBean.getCourse());
        List<String> imgList = new ArrayList<>();
        for (PropertyBean propertyBean : properties) {
            if (propertyBean.getPredicateLabel().equals("图片") && !imgList.contains(propertyBean.getObject())) {
                imgList.add(propertyBean.getObject());
            }
        }
        properties.removeIf(p -> (p.getObject().contains("http://")));
        ViewPager viewPager = findViewById(R.id.viewPager);
        TextView textView = findViewById(R.id.image_tag);
        if (imgList.isEmpty()) {
            viewPager.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        } else {
            ImageAdapter image_adapter = new ImageAdapter(getApplicationContext(), imgList);
            viewPager.setAdapter(image_adapter);
        }

        relation_adapter = new EntityRelationAdapter(relations, entityBean.getCourse(), getApplicationContext());
        if(relations.isEmpty())
            findViewById(R.id.relation_tag).setVisibility(View.GONE);
        property_adapter = new EntityPropertyAdapter(properties);
        if(properties.isEmpty())
            findViewById(R.id.property_tag).setVisibility(View.GONE);
        problem_adpater = new ProblemAdapter(problems);
        if(problems.isEmpty())
            findViewById(R.id.problem_tag).setVisibility(View.GONE);
        System.out.println("onNext!" + entityBean.getProblems());
        initView();
        initRecyclerView();
        // 添加历史记录
        if (!userRepository.getUser().getHistories().contains(entityBean)) {
            userRepository.addHistory(entityBean);
        }
    }

}
