package com.g2.taskstrackermvvm.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.model.SubTask
import com.g2.taskstrackermvvm.model.Tag
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.utils.toEditable
import com.g2.taskstrackermvvm.viewmodel.UpdateTaskViewModel
import kotlinx.android.synthetic.main.fragment_update_task.*
import kotlinx.android.synthetic.main.subtask_card.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdateTaskFragment : Fragment() {

    private val args: UpdateTaskFragmentArgs by navArgs()
    private lateinit var task: LiveData<Task>
    private val status = listOf<Task.Status>(Task.Status.Todo, Task.Status.Doing, Task.Status.Done)

    private var layoutM: RecyclerView.LayoutManager = LinearLayoutManager(activity)
    private lateinit var subTaskAdapter: SubtaskAdapter
    private val subTasks: MutableList<SubTask> = mutableListOf()

    private val viewModel: UpdateTaskViewModel by viewModel()

    private fun rmSubtaskFromTask(id: String) {
        viewModel.rmSubtask(args.taskId, id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_task, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subTaskAdapter = SubtaskAdapter(subTasks, ::rmSubtaskFromTask)

        subtasksView.apply {
            layoutManager = layoutM
            adapter = subTaskAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        task = viewModel.getTask(args.taskId)
        statusSpinner.adapter =
            context?.let { ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, status) }
        task.observe(viewLifecycleOwner, Observer {
            tasknameEditText.text = it.title.toEditable()
            descEditText.text = it.desc.toEditable()
            statusSpinner.setSelection(status.indexOf(it.status))
            subTasks.clear()
            subTasks.addAll(it.subTasks)
            subTaskAdapter.notifyDataSetChanged()
            val tags = it.tagIds.map { id -> viewModel.getTag(id) }
            tags.filterNotNull()
            val tagsName = tags.map { tag -> tag?.name }
            val tagsColor = tags.map { tag ->
                when (tag?.color) {
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
            task_tags_container.setTags(tagsName, tagsColor)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UpdateTaskFragment()
    }

    class SubtaskAdapter(
        private val subtaskData: List<SubTask>,
        private val rmSubtask: (String) -> Unit
    ) :
        RecyclerView.Adapter<SubtaskAdapter.ViewHolder>() {
        class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v =
                LayoutInflater.from(parent.context).inflate(R.layout.subtask_card, parent, false)
            return ViewHolder(v)
        }

        override fun getItemCount(): Int = subtaskData.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.v.apply {
                subtask_status_cb.isChecked = when (subtaskData[position].status) {
                    SubTask.Status.UNFINISHED -> false
                    SubTask.Status.FINISH -> true
                }
                subtask_name_text.text = subtaskData[position].name.toEditable()
                subtask_rm_btn.setOnClickListener {
                    rmSubtask(subtaskData[position].id)
                }
            }
        }
    }
}

