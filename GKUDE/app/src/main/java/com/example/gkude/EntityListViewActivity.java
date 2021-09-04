package com.example.gkude;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gkude.adapter.EntityCollectionAdapter;
import com.example.gkude.bean.EntityBean;
import com.example.gkude.server.Result;
import com.example.gkude.server.UserDataSource;
import com.example.gkude.server.UserRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class EntityListViewActivity extends AppCompatActivity implements EntityCollectionAdapter.OnEntitySelectedListener {

    private List<EntityBean> entityList;
    private EntityCollectionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_searched);
        boolean isFavorite = getIntent().getBooleanExtra("isFavorite", true);
        UserRepository userRepository = UserRepository.getInstance(new UserDataSource());
        if (isFavorite) {
            entityList = userRepository.getUser().getFavorites();
            // sync TODO 把同步部分移到下拉刷新中去，下同
            Result<List<EntityBean>> result = userRepository.syncFavorites();
            if (result.getStatus().equals(200)) {
                entityList = result.getData();
                Toast.makeText(getApplicationContext(), "同步成功！",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "同步失败！",Toast.LENGTH_SHORT).show();
            }

        } else {
            entityList = userRepository.getUser().getHistories();
            // sync
            Result<List<EntityBean>> result = userRepository.syncHistories();
            if (result.getStatus().equals(200)) {
                entityList = result.getData();
                Toast.makeText(getApplicationContext(), "同步成功！",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "同步失败！",Toast.LENGTH_SHORT).show();
            }
        }
        initToolbar(isFavorite);
        initRecyclerView();
    }

    private void initToolbar(boolean isFavorite) {
        Toolbar toolbar = findViewById(R.id.entity_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        if (isFavorite) {
            toolbar.setTitle("我的收藏");
        } else {
            toolbar.setTitle("历史记录");
        }
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

    @Override
    public void onEntitySelected(EntityBean entity) {
        if(!entity.isVisited()){
            entity.save();
        }
        // Go to the detailed page
        Intent intent = new Intent(this, EntityViewActivity.class);
        intent.putExtra("entity_id", entity.getId());
        intent.putExtra("entity_label", entity.getLabel());
        intent.putExtra("entity_course", entity.getCourse());
        intent.putExtra("entity_uri", entity.getUri());
        startActivity(intent);
    }
}
