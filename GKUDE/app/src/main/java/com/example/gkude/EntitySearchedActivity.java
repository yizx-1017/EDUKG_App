package com.example.gkude;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gkude.adapter.EntityCollectionAdapter;
import com.example.gkude.bean.EntityBean;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class EntitySearchedActivity extends AppCompatActivity implements
        EntityCollectionAdapter.OnEntitySelectedListener {
    private String keyword;
    private List<EntityBean> entityList = new LinkedList<>();
    private EntityCollectionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_searched);
        keyword = getIntent().getStringExtra("keyword");
        initToolbar();
        initRecyclerView();
        initObserver();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.entity_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle("实体搜索结果");
    }

    private void initRecyclerView() {

        // Set layout manager
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Set adapter for recyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new EntityCollectionAdapter(entityList, this);
        recyclerView.setAdapter(mAdapter);
    }

    private void initObserver() {
        Observer<List<EntityBean>> observer = new Observer<List<EntityBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<EntityBean> entities) {
                mAdapter.setEntityList(entities);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
        // TODO(zixuanyi): Manage.getEntity
//        Manager.getEntity(observer, keyword);
    }

    @Override
    public void onEntitySelected(EntityBean entity) {
        if(!entity.getVisited()){
            entity.save();
        }
        // Go to the detailed page
        Intent intent = new Intent(this, EntityViewActivity.class);
        intent.putExtra("label", entity.getLabel());
        intent.putExtra("description", entity.getDescription());
        intent.putExtra("properties", (Parcelable) entity.getProperties());
        intent.putExtra("relations", (Parcelable) entity.getRelations());
        intent.putExtra("problems", (Parcelable) entity.getProblems());
        startActivity(intent);
    }

}
