package com.g2.taskstrackermvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.card_task.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.task_item_recycler_view_home.view.descText
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()
    private var data: MutableList<Task> = mutableListOf()
    private val taskAdapter: TaskAdapter = TaskAdapter(data)
    private var layoutM: RecyclerView.LayoutManager = LinearLayoutManager(activity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createTaskFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        taskListRecyclerView.apply {
            layoutManager = layoutM
            adapter = taskAdapter
        }
        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            data.clear()
            data.addAll(it)
            print(data)
            taskAdapter.notifyDataSetChanged()
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment()
    }


    class TaskAdapter(private val data: List<Task>) :
        RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

        class TaskViewHolder(val v: View) : RecyclerView.ViewHolder(v)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_task, parent, false) as View
            return TaskViewHolder(v)
        }

        override fun getItemCount(): Int = data.size

        override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            holder.v.taskNameText.text = data[position].title
            holder.v.descText.text = data[position].desc
            holder.v.status.text = data[position].status.toString()
            holder.v.status.setOnClickListener {
                
            }
        }
    }
}
