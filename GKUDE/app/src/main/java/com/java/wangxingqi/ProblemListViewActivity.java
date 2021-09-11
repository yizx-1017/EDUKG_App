package com.java.wangxingqi;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.wangxingqi.adapter.ProblemAdapter;
import com.java.wangxingqi.bean.ProblemBean;
import com.java.wangxingqi.server.Result;
import com.java.wangxingqi.server.UserDataSource;
import com.java.wangxingqi.server.UserRepository;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;
import java.util.Objects;

public class ProblemListViewActivity extends AppCompatActivity {
    private final String TAG = "ProblemListView";
    private List<ProblemBean> problemList;
    private ProblemAdapter mAdapter;
    private UserRepository userRepository;
    private RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.problem_list_view);
        userRepository = UserRepository.getInstance(new UserDataSource());
        problemList = userRepository.getUser().getWrongProblems();
        initToolbar();
        initRecyclerView();
        initSwipeRefresh(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        syncEntityList();
        initRecyclerView();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.entity_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle("错题本");
    }

    private void initRecyclerView() {
        // Set layout manager
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Set adapter for recyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ProblemAdapter(problemList);
        recyclerView.setAdapter(mAdapter);
    }
    private void initSwipeRefresh(ProblemListViewActivity rootView) {
        Log.i("EntityListView","initSwipe");
        refreshLayout = rootView.findViewById(R.id.swipe_refresh2);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            Log.e("refresh swipe", "onRefresh");
            syncEntityList();
            initRecyclerView();
        });
    }

    private void syncEntityList() {
        problemList = userRepository.getUser().getWrongProblems();
        // sync
        Result<List<ProblemBean>> result = userRepository.syncWrongProblems();
        if (result.getStatus().equals(200)) {
            problemList = result.getData();
            Toast.makeText(getApplicationContext(), "同步成功！",Toast.LENGTH_SHORT).show();
            refreshLayout.finishRefresh(true);
        } else {
            Toast.makeText(getApplicationContext(), "同步失败！",Toast.LENGTH_SHORT).show();
            refreshLayout.finishRefresh(false);
        }
    }
}
