package com.g2.taskstrackermvvm.view.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.model.SubTask
import com.g2.taskstrackermvvm.model.Tag
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.utils.toEditable
import com.g2.taskstrackermvvm.viewmodel.CreateTaskViewModel
import kotlinx.android.synthetic.main.create_new_task.*
import kotlinx.android.synthetic.main.create_new_task.view.*
import kotlinx.android.synthetic.main.dialog_add_subtask.view.*
import kotlinx.android.synthetic.main.dialog_select_tag.view.*
import kotlinx.android.synthetic.main.subtask_card.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class CreateTaskFragment : Fragment() {

    private val viewModel: CreateTaskViewModel by viewModel()
    private val status = listOf<Task.Status>(Task.Status.Todo, Task.Status.Doing, Task.Status.Done)
    private val tagsData: MutableList<Tag> = mutableListOf()
    private val selectableTags: MutableList<Tag> = mutableListOf()
    private val bindedTag: MutableList<Tag> = mutableListOf()
    private val subtasks: MutableList<SubTask> = mutableListOf()
    private lateinit var subtaskAdapter: SubtaskAdapter
    private val selectedDate: Date = Date()

    private fun removeSubtask(pos: Int) {
        subtasks.removeAt(pos)
        subtaskAdapter.notifyDataSetChanged()
    }

    private fun updateSubtaskStatus(pos: Int, status: SubTask.Status) {
        subtasks[pos].status = status
        subtaskAdapter.notifyDataSetChanged()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.create_new_task, container, false)
        subtaskAdapter = SubtaskAdapter(
            subtasks,
            ::removeSubtask,
            ::updateSubtaskStatus
        )

        v.subtasksView_create_task.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = subtaskAdapter
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statusSpinner_create_task.adapter =
            context?.let { ArrayAdapter(it, R.layout.status_spinner_item, status) }

        add_subtask_btn.setOnClickListener {
            val v = LayoutInflater.from(context).inflate(R.layout.dialog_add_subtask, null)
            AlertDialog.Builder(context).apply {
                setView(v)
                setTitle("Add Subtask")
                setPositiveButton(R.string.add) { dialog, _ ->
                    val subtask = SubTask()
                    subtask.name = v.subtask_name_edit_text.text.toString()
                    subtasks.add(subtask)
                    subtaskAdapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                create()
            }.show()
        }

        addTagBtn2.setOnClickListener {
            val v =
                LayoutInflater.from(context).inflate(R.layout.dialog_select_tag, null)

            val tagsContainer: TagContainerLayout = v.select_tag_container
            tagsContainer.backgroundColor = Color.WHITE

            val tagNames = selectableTags.map { tag -> tag.name }
            val tagColors = selectableTags.map { tag ->
                when (tag.color) {
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

            tagsContainer.setTags(tagNames, tagColors)

            val selectedTag: MutableList<Int> = mutableListOf()
            tagsContainer.setOnTagClickListener(object : TagView.OnTagClickListener {
                override fun onSelectedTagDrag(position: Int, text: String?) {
                }

                override fun onTagLongClick(position: Int, text: String?) {
                }

                override fun onTagClick(position: Int, text: String?) {
                    val selected = tagsContainer.getTagView(position)
                    if (position in selectedTag) {
                        selected?.setTagBorderColor(
                            Color.BLACK
                        )
                        selectedTag.remove(position)
                        return
                    }

                    selectedTag.add(position)

                    selected?.setTagBorderColor(
                        Color.YELLOW
                    )
                }

                override fun onTagCrossClick(position: Int) {
                }
            })
            AlertDialog.Builder(context).apply {
                setView(v)
                setTitle("Select Tag")
                setPositiveButton("Set") { dialog, _ ->
                    bindedTag.clear()
                    bindedTag.addAll(selectedTag.map { pos -> selectableTags[pos] })
                    selectableTags.retainAll { tag ->
                        tag !in bindedTag
                    }
                    task_tags_container_create_task.setTags(
                        bindedTag.map { tag -> tag.name },
                        bindedTag.map { tag ->
                            when (tag.color) {
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
                            }
                        })

                    dialog.dismiss()
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                create()
            }.show()
        }

        due_date_edit_text.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val min = c.get(Calendar.MINUTE)
            val sec = c.get(Calendar.SECOND)

            activity?.let { activity ->
                DatePickerDialog(activity, { _, year, monthOfYear, dayOfMonth ->
                    selectedDate.apply {
                        this.year = year
                        this.month = monthOfYear
                        this.date = dayOfMonth
                        TimePickerDialog(activity, { _, hourOfDay, min ->
                            selectedDate.hours = hourOfDay
                            selectedDate.minutes = min
                            due_date_edit_text.text = selectedDate.toString().toEditable()
                        }, hour, min, DateFormat.is24HourFormat(activity)).show()
                    }
                }, year, month, day).show()
            }
        }

        viewModel.tagList.observe(viewLifecycleOwner, Observer {
            tagsData.clear()
            tagsData.addAll(it)
            selectableTags.clear()
            selectableTags.addAll(it)
            selectableTags.retainAll { tag ->
                tag !in bindedTag
            }
        })



        createTaskBtn.setOnClickListener {
            if (titleEditText_create_task.text.toString() == "") {
                Toast.makeText(context, "Title is empty", Toast.LENGTH_SHORT).show()
            } else {
                val task = Task()
                task.title = titleEditText_create_task.text.toString()
                task.desc = descEditText_create_task.text.toString()
                task.status = statusSpinner_create_task.selectedItem as Task.Status
                bindedTag.forEach {
                    task.addTag(it.id)
                }
                subtasks.forEach {
                    task.addSubtask(it)
                }

                viewModel.createNewTask(
                    task
                )
                findNavController().navigateUp()
            }
        }
        cancelCreateTaskBtn.setOnClickListener {
            findNavController().navigate(R.id.action_createTaskFragment_to_homeFragment)
        }
    }

    class SubtaskAdapter(
        private val subtaskData: MutableList<SubTask>,
        private val rmSubtask: (Int) -> Unit,
        private val updateSubtask: (Int, SubTask.Status) -> Unit
    ) :
        RecyclerView.Adapter<SubtaskAdapter.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v =
                LayoutInflater.from(parent.context).inflate(R.layout.subtask_card, parent, false)
            return ViewHolder(v)
        }

        override fun getItemCount(): Int = subtaskData.size
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.v.apply {
                subtask_status_cb.apply {
                    isChecked = when (subtaskData[position].status) {
                        SubTask.Status.UNFINISHED -> false
                        SubTask.Status.FINISH -> true
                    }
                    setOnCheckedChangeListener { buttonView, isChecked ->
                        updateSubtask(
                            position,
                            if (isChecked) SubTask.Status.FINISH else SubTask.Status.UNFINISHED
                        )
                    }
                }
                subtask_name_text.text = subtaskData[position].name.toEditable()
                subtask_rm_btn.setOnClickListener {
                    rmSubtask(position)
                }
            }
        }

        class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateTaskFragment()
    }
}
