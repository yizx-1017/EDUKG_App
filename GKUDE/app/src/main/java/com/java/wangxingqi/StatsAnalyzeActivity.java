package com.java.wangxingqi;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.java.wangxingqi.server.UserDataSource;
import com.java.wangxingqi.server.UserRepository;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class StatsAnalyzeActivity extends AppCompatActivity {
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        userRepository = UserRepository.getInstance(new UserDataSource());
        PieChart entityPieChart = findViewById(R.id.piechart1);
        PieChart problemPieChart = findViewById(R.id.piechart2);
        initToolbar();
        initData(entityPieChart, userRepository.getUser().getHistoryNum());
        initData(problemPieChart, userRepository.getUser().getWrongProblemNum());
    }
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.entity_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle("统计分析");
    }

    private void initData(PieChart pieChart, Map<String, Integer> mp) {
        ArrayList<PieEntry> entities = new ArrayList<>();
        String[] coursesCN = new String[]{"语文", "英语", "数学", "物理", "化学", "生物", "历史", "地理","政治"};
        String[] coursesEN = new String[]{"chinese", "english", "math", "physics", "chemistry", "biology", "history", "politics", "geo"};
        int[] colors = {
                Color.rgb(153, 195,217), Color.rgb(137,215,185)
        };
        for (int i = 0; i < coursesCN.length; i++) {
            entities.add(new PieEntry(mp.get(coursesEN[i]), coursesCN[i]));
        }
        PieDataSet pieDataSet = new PieDataSet(entities, "实体数据");
        // TODO: add more colors;
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setCenterText("实体数据");
        pieChart.animate();
    }
}
