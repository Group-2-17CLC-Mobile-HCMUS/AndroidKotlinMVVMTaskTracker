package com.g2.taskstrackermvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.viewmodel.ChartsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChartsFragment : Fragment() {
    private val viewModel: ChartsFragmentViewModel by viewModel()
    private val type = arrayOf("Status", "Day", "Month", "Year")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.report_chart_screen, container, false)
        val spinner = v.findViewById<Spinner>(R.id.chart)
        spinner?.adapter = activity?.applicationContext?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                type
            )
        } as SpinnerAdapter
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val choosed = parent?.getItemAtPosition(position).toString()
                if (choosed == "Status") {
                    childFragmentManager.commit {
                        replace(R.id.chart_fragment_container, TaskStatusChartFragment())
                    }
                }
                if (choosed == "Day") {
                    childFragmentManager.commit {
                        replace(R.id.chart_fragment_container, TaskDayChartFragment())
                    }
                }
                if (choosed == "Month") {
                    childFragmentManager.commit {
                        replace(R.id.chart_fragment_container, TaskMonthChartFragment())
                    }
                }
                if (choosed == "Year") {
                    childFragmentManager.commit {
                        replace(R.id.chart_fragment_container, TaskYearChartFragment())
                    }
                }
            }
        }
        return v;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}