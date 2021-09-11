package com.java.wangxingqi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.wangxingqi.adapter.EntityCollectionAdapter;
import com.java.wangxingqi.bean.EntityBean;
import com.java.wangxingqi.server.Result;
import com.java.wangxingqi.server.UserDataSource;
import com.java.wangxingqi.server.UserRepository;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class EntityListViewActivity extends AppCompatActivity implements EntityCollectionAdapter.OnEntitySelectedListener {

    private final String TAG = "EntityListView";
    private List<EntityBean> entityList;
    private EntityCollectionAdapter mAdapter;
    private Boolean isFavorite;
    private UserRepository userRepository;
    private RefreshLayout refreshLayout;
    private Observer<List<EntityBean>> observer;
    private String testCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entity_list_view);
        isFavorite = getIntent().getBooleanExtra("isFavorite", true);
        userRepository = UserRepository.getInstance(new UserDataSource());
        Log.i(TAG, isFavorite.toString());
        if (isFavorite.equals(true)) {
            entityList = userRepository.getUser().getFavorites();
        } else {
            entityList = userRepository.getUser().getHistories();
        }
        initToolbar(isFavorite);
        initObserver();
        initRecyclerView();
        initSwipeRefresh(this);
    }

    @Override
    public void onEntitySelected(EntityBean entity) {
        long entity_id = -1L;
        Log.i(TAG, entity.getLabel());
        System.out.println(entity.getId());
        if (entity.getId() == null) {
            Log.i(TAG, "before searchEntity");
            Manager.searchEntity(entity.getCourse(), entity.getLabel(), null, true, observer);
            Log.i(TAG, "after searchEntity");
            if (testCategory == null) {
                Toast.makeText(this, "处于断网状态，该实体未被缓存，无法获取", Toast.LENGTH_SHORT).show();
                return;
            } else {
                testCategory = null;
            }
        } else {
            entity_id = entity.getId();
        }
        // Go to the detailed page
        Intent intent = new Intent(this, EntityViewActivity.class);
        intent.putExtra("entity_id", entity_id);
        intent.putExtra("entity_label", entity.getLabel());
        intent.putExtra("entity_course", entity.getCourse());
        intent.putExtra("entity_category", entity.getCategory());
        intent.putExtra("entity_uri", entity.getUri());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        syncEntityList();
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
    private void initSwipeRefresh(EntityListViewActivity rootView) {
        Log.i("EntityListView","initSwipe");
        refreshLayout = rootView.findViewById(R.id.swipe_refresh2);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.e("refresh swipe", "onRefresh");
                syncEntityList();
                initRecyclerView();
            }
        });
    }

    private void syncEntityList() {
        if (isFavorite) {
            entityList = userRepository.getUser().getFavorites();
            // sync
            Result<List<EntityBean>> result = userRepository.syncFavorites();
            if (result.getStatus().equals(200)) {
                entityList = result.getData();
                Toast.makeText(getApplicationContext(), "同步成功！",Toast.LENGTH_SHORT).show();
                refreshLayout.finishRefresh(true);
            } else {
                Toast.makeText(getApplicationContext(), "同步失败！",Toast.LENGTH_SHORT).show();
                refreshLayout.finishRefresh(false);
            }

        } else {
            entityList = userRepository.getUser().getHistories();
            // sync
            Result<List<EntityBean>> result = userRepository.syncHistories();
            if (result.getStatus().equals(200)) {
                entityList = result.getData();
                Toast.makeText(getApplicationContext(), "同步成功！",Toast.LENGTH_SHORT).show();
                refreshLayout.finishRefresh(true);
            } else {
                Toast.makeText(getApplicationContext(), "同步失败！",Toast.LENGTH_SHORT).show();
                refreshLayout.finishRefresh(false);
            }
        }
    }
    private void initObserver() {
        observer = new Observer<List<EntityBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull List<EntityBean> entities) {
                Log.i("RelationObserver", "onNext");
                if (entities.isEmpty()) {
                    Log.i("onNext","emptyList");
                    testCategory = null;
                } else {
                    Log.i("onNext","nonemptyList");
                    testCategory = entities.get(0).getCategory();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };

    }
}
