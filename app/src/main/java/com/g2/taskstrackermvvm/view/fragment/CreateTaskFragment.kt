package com.g2.taskstrackermvvm.view.fragment

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.model.Tag
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.viewmodel.CreateTaskViewModel
import kotlinx.android.synthetic.main.create_new_task.*
import kotlinx.android.synthetic.main.dialog_select_tag.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreateTaskFragment : Fragment() {

    private val viewModel: CreateTaskViewModel by viewModel()
    private val status = listOf<Task.Status>(Task.Status.Todo, Task.Status.Doing, Task.Status.Done)
    private val tagsData: MutableList<Tag> = mutableListOf()
    private val selectableTags: MutableList<Tag> = mutableListOf()
    private val bindedTag: MutableList<Tag> = mutableListOf()
    private var layoutM: RecyclerView.LayoutManager = LinearLayoutManager(activity)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.create_new_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statusSpinner_create_task.adapter =
            context?.let { ArrayAdapter(it, R.layout.status_spinner_item, status) }

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
//            if (titleEditText.text.toString() == "") {
//                Toast.makeText(context, "Title is empty", Toast.LENGTH_SHORT).show()
//            } else {
//
//                viewModel.addTask(
//                    title = titleEditText.text.toString(),
//                    desc = taskDescEditText.text.toString()
//                )
//                findNavController().navigateUp()
//            }
        }
        cancelCreateTaskBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateTaskFragment()
    }
}
