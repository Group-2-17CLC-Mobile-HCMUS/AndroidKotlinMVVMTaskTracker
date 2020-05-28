package com.g2.taskstrackermvvm.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.viewmodel.PieChartViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.pie_chart.*

class PieChartFragment : Fragment() {

    companion object {
        fun newInstance() = PieChartFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var viewModel: PieChartViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.pie_chart, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pieChart.setUsePercentValues(true);

        val desc = Description();
        desc.text = "Statistic";
        desc.textSize = 20f;
        pieChart.description = desc;
        pieChart.holeRadius = 25f;


        val value = ArrayList<PieEntry>();
        value.add(PieEntry(32f, "To Do"));
        value.add(PieEntry(46f, "Doing"));
        value.add(PieEntry(22f, "Done"));

        val pieDataSet = PieDataSet(value, "Status");
        val pieData = PieData(pieDataSet);
        pieChart.data = pieData;
        pieDataSet.setColors(Color.BLUE);
        pieChart.animateXY(1000, 1400);
    }
}