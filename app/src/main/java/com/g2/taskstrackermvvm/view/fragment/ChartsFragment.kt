package com.g2.taskstrackermvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.viewmodel.ChartsFragmentViewModel

import kotlinx.android.synthetic.main.fragment_task_day_chart.view.*
import kotlinx.android.synthetic.main.fragment_task_status_chart.view.*
import kotlinx.android.synthetic.main.report_chart_screen.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChartsFragment : Fragment() {
    private val viewModel: ChartsFragmentViewModel by viewModel()
    private val type = arrayOf("Status", "Day", "Month","Year")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.report_chart_screen, container, false)
        val spinner = v.findViewById<Spinner>(R.id.chart)
        spinner?.adapter =  activity?.applicationContext?.let { ArrayAdapter(it,R.layout.support_simple_spinner_dropdown_item, type) } as SpinnerAdapter
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val choosed = parent?.getItemAtPosition(position).toString()
                println(choosed)
                if (choosed == "Status") {
                    val view = layoutInflater.inflate(R.layout.fragment_task_status_chart, null)
                    view.status_pie_chart.apply {
                        TaskStatusChartFragment()
                    }
                }
                if (choosed == "Day") {
                    val view = layoutInflater.inflate(R.layout.fragment_task_day_chart, null)
                    view.day_pie_chart.apply {
                        TaskDayChartFragment()
                    }
                }
                if (choosed == "Month") {
                    val view = layoutInflater.inflate(R.layout.fragment_task_month_chart, null)
                    view.day_pie_chart.apply {
                        TaskDayChartFragment()
                    }

                }
                if (choosed == "Year") {
                    val view = layoutInflater.inflate(R.layout.fragment_task_year_chart, null)
                    view.day_pie_chart.apply {
                        TaskDayChartFragment()
                    }
                }
            }
        }
        return v;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}