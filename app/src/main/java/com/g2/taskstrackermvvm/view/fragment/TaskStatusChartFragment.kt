package com.g2.taskstrackermvvm.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.viewmodel.TaskStatusChartViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_task_status_chart.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class TaskStatusChartFragment : Fragment() {

    private val viewModel: TaskStatusChartViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_status_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.tasks.observe(viewLifecycleOwner, Observer {


            if (it.isNotEmpty()) {
                var todo = 0
                var doing = 0
                var done = 0

                for (item in it) {
                    when (item.status) {
                        Task.Status.Todo -> ++todo
                        Task.Status.Doing -> ++doing
                        Task.Status.Done -> ++done
                    }
                }

                val perTodo = todo * 1.0 / it.size
                val perDoing = doing * 1.0 / it.size
                val perDone = done * 1.0 / it.size

                val entries = listOf<PieEntry>(
                    PieEntry(perTodo.toFloat(), "ToDo"),
                    PieEntry(perDoing.toFloat(), "Doing"),
                    PieEntry(perDone.toFloat(), "Done")
                )
                val dataset = PieDataSet(entries, "Status")
                val colors = listOf<Int>(Color.RED, Color.GREEN, Color.BLUE)
                dataset.colors = colors
                val data = PieData(dataset)
                data.setValueTextSize(12f)
                status_pie_chart.data = data
                context?.let { context ->
                    status_pie_chart.setCenterTextColor(
                        resources.getColor(
                            R.color.background_color,
                            context.theme
                        )
                    )
                }
                status_pie_chart.setDrawRoundedSlices(true)
                status_pie_chart.setDrawSlicesUnderHole(true)
                status_pie_chart.centerText = "Overall"
                status_pie_chart.invalidate()

            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TaskStatusChartFragment()
    }
}
