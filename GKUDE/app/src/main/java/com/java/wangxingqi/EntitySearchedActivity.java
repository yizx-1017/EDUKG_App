package com.java.wangxingqi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.java.wangxingqi.adapter.EntityCollectionAdapter;
import com.java.wangxingqi.bean.EntityBean;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class EntitySearchedActivity extends AppCompatActivity implements
        EntityCollectionAdapter.OnEntitySelectedListener {
    private String keyword, course, sort;

    private List<EntityBean> entityList = new LinkedList<>();
    private EntityCollectionAdapter mAdapter;
    private RefreshLayout refreshLayout;
    private Comparator<? super EntityBean> comparator;
    private Observer<List<EntityBean>> observer;
    private Spinner spinner_sorter, spinner_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_searched);
        keyword = getIntent().getStringExtra("keyword");
        course = getIntent().getStringExtra("course");
        sort = getIntent().getStringExtra("sort");
        comparator = null;
        if(sort.equals("abc")) comparator = Comparator.comparing(EntityBean::getLabel);
        else if(sort.equals("length")) comparator = Comparator.comparing(EntityBean::getLabel, Comparator.comparingInt(String::length));
        initSpinner();
        initToolbar();
        initRecyclerView();
        initObserver();
        initSwipeRefresh();
    }

    private void initSpinner() {
        spinner_filter = findViewById(R.id.spin_filter);
        spinner_sorter = findViewById(R.id.spin_sorter);
        ArrayAdapter<CharSequence> spinnerFilterAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.course, R.layout.my_support_simple_spinner_dropdown_item);
        spinner_filter.setAdapter(spinnerFilterAdapter);
        switch(course){
            case "chinese":
                spinner_filter.setSelection(0, true);
                break;
            case "math":
                spinner_filter.setSelection(1, true);
                break;
            case "english":
                spinner_filter.setSelection(2, true);
                break;
            case "physics":
                spinner_filter.setSelection(3, true);
                break;
            case "chemistry":
                spinner_filter.setSelection(4, true);
                break;
            case "biology":
                spinner_filter.setSelection(5, true);
                break;
            case "history":
                spinner_filter.setSelection(6, true);
                break;
            case "politics":
                spinner_filter.setSelection(7, true);
                break;
            case "geo":
                spinner_filter.setSelection(8, true);
                break;
        }
        ArrayAdapter<CharSequence> spinnerSorterAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sorters, R.layout.my_support_simple_spinner_dropdown_item);
        spinner_sorter.setAdapter(spinnerSorterAdapter);
        switch(sort) {
            case "normal":
                spinner_sorter.setSelection(0, true);
                break;
            case "abc":
                spinner_sorter.setSelection(1, true);
                break;
            case "length":
                spinner_sorter.setSelection(2, true);
                break;
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.entity_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle("搜索结果");
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

    private void initSwipeRefresh() {
        refreshLayout = findViewById(R.id.swipe_refresh2);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getApplicationContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getApplicationContext()));
        refreshLayout.setOnRefreshListener(refresh_layout -> {
            Log.e("EntitySearched", "onRefresh: ");
            Manager.searchEntity(course, keyword, comparator, true, observer);
            refreshLayout.finishRefresh(true);
        });
    }

    private void initObserver() {
        System.out.println("in initObserver");
        observer = new Observer<List<EntityBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull List<EntityBean> entities)
            {
                mAdapter.setEntityList(entities);
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
        System.out.println("searchEntity!!!");
        Manager.searchEntity(course, keyword, comparator, true, observer);

        spinner_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String courseCN = adapterView.getItemAtPosition(position).toString();
//                Toast.makeText(getContext(), "选择的学科是：" + courseCN, Toast.LENGTH_SHORT).show();
                switch (courseCN){
                    case "语文":
                        course = "chinese";
                        break;
                    case "数学":
                        course = "math";
                        break;
                    case "英语":
                        course = "english";
                        break;
                    case "物理":
                        course = "physics";
                        break;
                    case "化学":
                        course = "chemistry";
                        break;
                    case "生物":
                        course = "biology";
                        break;
                    case "历史":
                        course = "history";
                        break;
                    case "政治":
                        course = "politics";
                        break;
                    case "地理":
                        course = "geo";
                        break;
                }
                // TODO(zhiyuxie): 加上不重新搜索，只重新排序的优化
                Manager.searchEntity(course, keyword, comparator,true, observer);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_sorter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String courseCN = adapterView.getItemAtPosition(position).toString();
//                Toast.makeText(getContext(), "选择的排序是：" + courseCN, Toast.LENGTH_SHORT).show();
                switch (courseCN){
                    case "默认":
                        sort = "normal";
                        break;
                    case "字母序":
                        sort = "abc";
                        break;
                    case "长度":
                        sort = "length";
                        break;
                }
                comparator = null;
                if(sort.equals("abc")) comparator = Comparator.comparing(EntityBean::getLabel);
                else if(sort.equals("length")) comparator = Comparator.comparing(EntityBean::getLabel, Comparator.comparingInt(String::length));
                Manager.searchEntity(course, keyword, comparator, true,observer);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onEntitySelected(EntityBean entity) {
        System.out.println("entity clicked!!!!!!!!");
        // Go to the detailed page
        Intent intent = new Intent(this, EntityViewActivity.class);
        intent.putExtra("entity_id", entity.getId());
        intent.putExtra("entity_label", entity.getLabel());
        intent.putExtra("entity_course", entity.getCourse());
        intent.putExtra("entity_category", entity.getCategory());
        intent.putExtra("entity_uri", entity.getUri());
        startActivity(intent);
    }

}
