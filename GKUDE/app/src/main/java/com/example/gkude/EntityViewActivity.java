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

import org.javatuples.Triplet;

import java.util.List;
import java.util.Objects;

public class EntityViewActivity extends AppCompatActivity {
    private String label, description;
    private List<Triplet<String, Boolean, EntityBean>> relations;
    private List<Pair<String, String>> properties;
    private List<ProblemBean> problems;
    private EntityRelationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_view);
        // TODO(zhiyuxie): shouldn't pass string like this here. Create an observer and call Manager.getEntity()
//        label = getIntent().getStringExtra("label");
//        description = getIntent().getStringExtra("description");
//        properties = getIntent().getParcelableExtra("properties");
//        relations = getIntent().getParcelableExtra("relations");
//        problems = getIntent().getParcelableExtra("problems");
        initToolbar();
        initView();
        initRecyclerView();
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

//        TextView mLabel = findViewById(R.id.entity_label);
//        TextView mInfo = findViewById(R.id.entity_description);
//
//        mLabel.setText(label);
//        mInfo.setText(info);
//
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
