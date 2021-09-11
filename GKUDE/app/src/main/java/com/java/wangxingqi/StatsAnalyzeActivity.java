package com.java.wangxingqi;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.java.wangxingqi.server.UserDataSource;
import com.java.wangxingqi.server.UserRepository;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class StatsAnalyzeActivity extends AppCompatActivity {
    private UserRepository userRepository;
    private PieChart entityPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        userRepository = UserRepository.getInstance(new UserDataSource());
        entityPieChart = findViewById(R.id.piechart1);
        PieChart problemPieChart = findViewById(R.id.piechart2);
        initToolbar();
        initData(entityPieChart, userRepository.getUser().getHistoryNum());
        initData(problemPieChart, userRepository.getUser().getWrongProblemNum());
        entityPieChart.setCenterText("实体数据");
        problemPieChart.setCenterText("错题数据");
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
                Color.rgb(135,206,250), Color.rgb(137,215,185), Color.rgb(252,206,190),
                Color.rgb(255,241,221), Color.rgb(221,214,202), Color.rgb(244,109,144),
                Color.rgb(175,199,191), Color.rgb(238,223,218), Color.rgb(140,139,170)
        };
        for (int i = 0; i < coursesCN.length; i++) {
            if (!mp.getOrDefault(coursesEN[i], 0).equals(0)) {
                entities.add(new PieEntry(mp.getOrDefault(coursesEN[i], 0), coursesCN[i]));
            }
        }
        PieDataSet pieDataSet = new PieDataSet(entities, "");
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16);

        PieData pieData = new PieData(pieDataSet);
        if (pieChart == entityPieChart) {
            pieData.setValueFormatter(new MyValueFormatter());
        } else {
            pieData.setValueFormatter(new MyValueFormatter2());
        }

        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setData(pieData);
        pieChart.animate();
    }
}

class MyValueFormatter extends ValueFormatter {

    private DecimalFormat mFormat;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value) {
        // write your logic here
        return mFormat.format(value)+"个"; // e.g. append a dollar-sign
    }
}

class MyValueFormatter2 extends ValueFormatter {

    private DecimalFormat mFormat;

    public MyValueFormatter2() {
        mFormat = new DecimalFormat("###,###,##0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value) {
        // write your logic here
        return mFormat.format(value)+"题"; // e.g. append a dollar-sign
    }
}
