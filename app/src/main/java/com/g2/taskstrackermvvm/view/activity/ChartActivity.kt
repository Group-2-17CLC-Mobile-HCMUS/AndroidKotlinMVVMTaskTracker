package com.g2.taskstrackermvvm.view.activity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.view.fragment.TaskDayChartFragment
import com.g2.taskstrackermvvm.view.fragment.TaskStatusChartFragment
import kotlinx.android.synthetic.main.fragment_task_day_chart.view.*
import kotlinx.android.synthetic.main.fragment_task_status_chart.view.*
import kotlinx.android.synthetic.main.report_chart_screen.*

class ChartActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var type = arrayOf("Status", "Day", "Month","Year")

    var spinner: Spinner? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_chart_screen)
        spinner = this.chart
        spinner!!.setOnItemSelectedListener(this)

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, type)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner!!.setAdapter(aa)

    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        val tmp =  type[position];
        if (tmp == "Status")
        {
            val view = layoutInflater.inflate(R.layout.fragment_task_status_chart,null)
            view.status_pie_chart.apply {
                TaskStatusChartFragment()
            }
//            var fragment :Fragment?=null
//            if (fragment != null)
//            {val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.status_pie_chart,fragment)
//            transaction.commit()
//            }
        }
        if (tmp == "Day") {
            val view = layoutInflater.inflate(R.layout.fragment_task_day_chart, null)
            view.day_pie_chart.apply {
                TaskDayChartFragment()
            }
        }

    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }
}