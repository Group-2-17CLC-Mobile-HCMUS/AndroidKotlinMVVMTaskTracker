package com.g2.taskstrackermvvm.view.activity

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart;
import com.g2.taskstrackermvvm.R
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.pie_chart.*

class PieChartScreen  : Activity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_chart);
        var pieChart = findViewById<PieChart>(R.id.pieChart);
        pieChart.setUsePercentValues(true);

        var desc = Description();
        desc.setText("Statistic");
        desc.setTextSize(20f);
        pieChart.setDescription(desc);
        pieChart.setHoleRadius(25f);


        var value = ArrayList<PieEntry>();
        value.add(PieEntry(32f,"To Do"));
        value.add(PieEntry(46f,"Doing"));
        value.add( PieEntry(22f,"Done"));

        var pieDataSet =  PieDataSet(value,"Status");
        var pieData =  PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors()
        pieChart.animateXY(1000,1400);
    }
}