package com.g2.taskstrackermvvm.view.fragment

import android.graphics.Color
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
import com.g2.taskstrackermvvm.model.Tag
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.card_task.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.task_item_recycler_view_home.view.descText
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()
    private var data: MutableList<Task> = mutableListOf()
    private val taskAdapter: TaskAdapter =
        TaskAdapter(data, ::updateTaskStatus, ::removeTask, ::getTagById)
    private var layoutM: RecyclerView.LayoutManager = LinearLayoutManager(activity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun getTagById(id: String) : Tag? {
        return viewModel.getTagById(id)
    }

    private fun removeTask(pos: Int) {
        viewModel.removeTask(data[pos])
    }

    private fun updateTaskStatus(pos: Int) {
        val targetTask = data[pos]
        when (targetTask.status) {
            Task.Status.Todo -> targetTask.status = Task.Status.Doing
            Task.Status.Doing -> targetTask.status = Task.Status.Done
            Task.Status.Done -> targetTask.status = Task.Status.Todo
        }

        viewModel.updateTask(targetTask)
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


    class TaskAdapter(
        private val data: List<Task>,
        private val updateTaskStatus: (Int) -> Unit,
        private val removeTask: (Int) -> Unit,
        private val getTag: (String) -> Tag?
    ) :
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
                updateTaskStatus(position)
            }
            val tags = data[position].tagIds.map { id -> getTag(id) }
            val tagNames = tags.map { tag -> tag!!.name }
            val tagColors = tags.map { tag ->
                when (tag!!.color) {
                    Tag.Color.RED -> intArrayOf(
                        0xffff5131.toInt(),
                        Color.BLACK,
                        Color.BLACK,
                        Color.YELLOW
                    )
                    Tag.Color.BLUE -> intArrayOf(
                        0xff768fff.toInt(),
                        Color.BLACK,
                        Color.BLACK,
                        Color.YELLOW
                    )
                    Tag.Color.GREEN -> intArrayOf(
                        0xff5efc82.toInt(),
                        Color.BLACK,
                        Color.BLACK,
                        Color.YELLOW
                    )
                    else -> intArrayOf(
                        0xffaeaeae.toInt(),
                        Color.BLACK,
                        Color.BLACK,
                        Color.YELLOW
                    )
                }
            }
            holder.v.task_tags_container.setTags(tagNames, tagColors)
            holder.v.setOnCreateContextMenuListener { contextMenu, _, _ ->
                contextMenu.apply {
                    add("Modify").setOnMenuItemClickListener {
                        print("Modify $position")
                        true
                    }
                    add("Remove").setOnMenuItemClickListener {
                        removeTask(position)
                        true
                    }
                }
            }
        }
    }
}
