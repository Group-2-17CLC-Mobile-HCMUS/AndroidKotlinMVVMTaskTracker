package com.g2.taskstrackermvvm.view.fragment

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.model.SubTask
import com.g2.taskstrackermvvm.model.Tag
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.utils.toEditable
import com.g2.taskstrackermvvm.viewmodel.UpdateTaskViewModel
import kotlinx.android.synthetic.main.dialog_select_tag.view.*
import kotlinx.android.synthetic.main.fragment_update_task.*
import kotlinx.android.synthetic.main.fragment_update_task.view.*
import kotlinx.android.synthetic.main.subtask_card.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdateTaskFragment : Fragment() {

    private val args: UpdateTaskFragmentArgs by navArgs()
    private lateinit var task: LiveData<Task>
    private var currentTask: Task? = null
    private val status = listOf<Task.Status>(Task.Status.Todo, Task.Status.Doing, Task.Status.Done)
    private var layoutM: RecyclerView.LayoutManager = LinearLayoutManager(activity)
    private lateinit var subTaskAdapter: SubtaskAdapter
    private val subTasks: MutableList<SubTask> = mutableListOf()
    private val tagsData: MutableList<Tag> = mutableListOf()
    private val selectableTags: MutableList<Tag> = mutableListOf()
    private val bindedTag: MutableList<Tag> = mutableListOf()
    private val viewModel: UpdateTaskViewModel by viewModel()
    private var isEditable: Boolean = false

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
        val v = inflater.inflate(R.layout.fragment_update_task, container, false)
        v.titleEditText_update_task.isEnabled = false
        v.descEditText_update_task.isEnabled = false
        v.addTagBtn.visibility = View.GONE
        v.removeTagBtn.visibility = View.GONE
        v.addSubtaskBtn.visibility = View.GONE
        v.statusSpinner_update_task.isEnabled = false
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subTaskAdapter = SubtaskAdapter(subTasks, ::rmSubtaskFromTask)

        updateButton.setOnClickListener {
            if (isEditable) {
                titleEditText_update_task.isEnabled = false
                descEditText_update_task.isEnabled = false
                addTagBtn.visibility = View.GONE
                removeTagBtn.visibility = View.GONE
                addSubtaskBtn.visibility = View.GONE
                statusSpinner_update_task.isEnabled = false
                updateButton.setText(R.string.update)
            } else {
                titleEditText_update_task.isEnabled = true
                descEditText_update_task.isEnabled = true
                addTagBtn.visibility = View.VISIBLE
                removeTagBtn.visibility = View.VISIBLE
                addSubtaskBtn.visibility = View.VISIBLE
                statusSpinner_update_task.isEnabled = true
                updateButton.setText(R.string.view)
            }
            isEditable = !isEditable
        }

        subtasksView_update_task.apply {
            layoutManager = layoutM
            adapter = subTaskAdapter
        }

        addTagBtn.setOnClickListener {
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
                    viewModel.setTags(
                        args.taskId,
                        selectedTag.map { pos -> selectableTags[pos].id })
                    dialog.dismiss()
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                create()
            }.show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        task = viewModel.getTask(args.taskId)
        statusSpinner_update_task.adapter =
            context?.let { ArrayAdapter(it, R.layout.status_spinner_item, status) }
        statusSpinner_update_task.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currentTask?.let {
                        currentTask?.status = status[position]
                        viewModel.updateTask(currentTask)
                    }
                }
            }

        titleEditText_update_task.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    currentTask?.let {
                        currentTask?.title = titleEditText_update_task.text.toString()
                        viewModel.updateTask(currentTask)
                    }
                }
            }

        descEditText_update_task.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                currentTask?.let {
                    currentTask?.desc = descEditText_update_task.text.toString()
                    viewModel.updateTask(currentTask)
                }
            }
        }

        task.observe(viewLifecycleOwner, Observer { it ->
            currentTask = it
            titleEditText_update_task.text = it.title.toEditable()
            descEditText_update_task.text = it.desc.toEditable()
            statusSpinner_update_task.setSelection(status.indexOf(it.status))
            subTasks.clear()
            subTasks.addAll(it.subTasks)
            subTaskAdapter.notifyDataSetChanged()
            var tags = it.tagIds.map { id -> viewModel.getTag(id) }
            tags = tags.filterNotNull()
            bindedTag.clear()
            tags.forEach { tag ->
                tag.let {
                    bindedTag.add(tag)
                }
            }
            selectableTags.retainAll { tag ->
                tag !in bindedTag
            }

            val tagsName = tags.map { tag -> tag.name }
            val tagsColor = tags.map { tag ->
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

            task_tags_container_update_task.setTags(tagsName, tagsColor)
        })
        viewModel.tagList.observe(viewLifecycleOwner, Observer {
            tagsData.clear()
            tagsData.addAll(it)
            selectableTags.clear()
            selectableTags.addAll(it)
            selectableTags.retainAll { tag ->
                tag !in bindedTag
            }
        })
    }

    class SubtaskAdapter(
        private val subtaskData: List<SubTask>,
        private val rmSubtask: (String) -> Unit
    ) :
        RecyclerView.Adapter<SubtaskAdapter.ViewHolder>() {

        private var isSubtaskRemovable: Boolean = false

        fun setSubtaskRemovable(flag: Boolean) {
            isSubtaskRemovable = flag
            notifyDataSetChanged()
        }

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
                subtask_rm_btn.visibility = if (isSubtaskRemovable) View.VISIBLE else View.GONE
            }
        }

        class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UpdateTaskFragment()
    }
}

