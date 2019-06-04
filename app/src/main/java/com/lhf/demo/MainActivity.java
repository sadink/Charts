package com.lhf.demo;

import android.app.Activity;
import android.os.Bundle;

import com.lhf.charts.R;
import com.lhf.charts.mode.entity.RoseEntity;
import com.lhf.charts.widget.rose.RoseChartView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindView(R.id.rose_chartView)
    RoseChartView roseChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        List<RoseEntity> listRoseLeafs = new ArrayList<>();
        listRoseLeafs.add(new RoseEntity("面粉", 0.19f, 0.33f, R.color.red_1));
        listRoseLeafs.add(new RoseEntity("白砂糖", 0.21f, 0.27f, R.color.orange_2));
        listRoseLeafs.add(new RoseEntity("果仁1", 0.18f, 0.20f, R.color.green_2));
        listRoseLeafs.add(new RoseEntity("果仁2", 0.28f, 0.13f, R.color.blue_3));
        listRoseLeafs.add(new RoseEntity("果仁3", 0.14f, 0.07f, R.color.purple_3));

        roseChartView.setDataRoseLeaf(listRoseLeafs);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @OnClick(R.id.rose_chartView)
    public void onClick() {
        roseChartView.executeAnim();
    }
}
