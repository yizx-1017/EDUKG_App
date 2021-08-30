package com.example.gkude;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
import com.example.gkude.adapter.EntityRelationAdapter;
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
    private int entity_id;
    private String label, description;
    private List<RelationBean> relations;
    private List<PropertyBean> properties;
    private List<ProblemBean> problems;
    private EntityRelationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_view);
        entity_id = getIntent().getIntExtra("entity_id", 1);
        initObserver();
        // TODO(zhiyuxie): check if data can be accessed here. thread problem?
        EntityBean entity = EntityBean.findById(EntityBean.class, entity_id);
        label = entity.getLabel();
        description = entity.getDescription();
        relations = entity.getRelationsFromStore();
        properties = entity.getPropertiesFromStore();
        problems = entity.getProblemsFromStore();

        initToolbar();
        initView();
        initRecyclerView();
    }

    private void initObserver() {
        Observer<EntityBean> observer = new Observer<EntityBean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(EntityBean entityBean) {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
        // TODO: check
        System.out.println("in EntityViewActivity.java, observer, prepare to getEntityInfo");
        Manager.getEntityInfo(EntityBean.findById(EntityBean.class, entity_id), observer);
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
        RecyclerView recyclerView = findViewById(R.id.recycler_view_relation);
        mAdapter = new EntityRelationAdapter(relations);
        recyclerView.setAdapter(mAdapter);
        RecyclerView rv = findViewById(R.id.recycler_view_property);
        // TODO(zixuanyi): set recyclerView for properties and problems
    }

    private void initView() {
        // TODO(zixuanyi): finish the content page of an entity


        TextView mLabel = findViewById(R.id.entity_label);
        TextView mInfo = findViewById(R.id.entity_description);

        mLabel.setText(label);
        mInfo.setText(description);

//        TextView mDef = findViewById(R.id.entity_definition_content);
//        TextView mFeature = findViewById(R.id.entity_relation);
//        TextView mInclude = findViewById(R.id.entity_include_content);
//        TextView mCondition = findViewById(R.id.entity_condition_content);
//        TextView mSpread = findViewById(R.id.entity_spread_content);
//        TextView mApplication = findViewById(R.id.entity_application_content);
//        if(prop != null) {
//            mDef.setText(prop.getDefinition());
//            mFeature.setText(prop.getFeature());
//            mInclude.setText(prop.getInclude());
//            mCondition.setText(prop.getCondition());
//            mSpread.setText(prop.getSpread());
//            mApplication.setText(prop.getApplication());
//        }
    }

}
