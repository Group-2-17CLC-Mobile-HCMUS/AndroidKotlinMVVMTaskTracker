package com.g2.taskstrackermvvm.view.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
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
    private var tagsData: MutableList<Tag> = mutableListOf()
    private lateinit var taskAdapter: TaskAdapter
    private var layoutM: RecyclerView.LayoutManager = LinearLayoutManager(activity)
    private val searchMode = listOf(
        HomeViewModel.SearchMode.TITLE,
        HomeViewModel.SearchMode.TAG
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun getTagById(id: String): Tag? {
        return viewModel.getTagById(id)
    }

    private fun setTag(taskId: String, tagId: String) {
        viewModel.setTag(taskId, tagId)
    }

    private fun removeTask(pos: Int) {
        viewModel.removeTask(data[pos])
    }

    private fun navigateToItemDetail(taskId: String) {
        val direction = HomeFragmentDirections.actionHomeFragmentToUpdateTaskFragment(taskId)
        findNavController().navigate(direction)
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
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_view.setIconifiedByDefault(false)
        search_view.findViewById<TextView>(R.id.search_src_text).apply {
            setTextColor(resources.getColor(R.color.colorText, context.theme))
        }
        search_view.findViewById<ImageView>(R.id.search_button).apply {
            setColorFilter(resources.getColor(R.color.colorText, context.theme))
        }
        search_view.findViewById<ImageView>(R.id.search_close_btn).apply {
            setColorFilter(resources.getColor(R.color.colorText, context.theme))
        }
        search_view.findViewById<ImageView>(R.id.search_mag_icon).apply {
            setColorFilter(resources.getColor(R.color.colorText, context.theme))
        }

        search_view.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.applySearch(searchMode[search_mode_spinner.selectedItemPosition], query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.applySearch(searchMode[search_mode_spinner.selectedItemPosition], newText)
                return true
            }
        })

        search_mode_spinner.apply {
            adapter = ArrayAdapter(context, R.layout.status_spinner_item, searchMode)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    viewModel.applySearch(HomeViewModel.SearchMode.TITLE, null)
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val key = if (search_view.query.isEmpty()) null else search_view.query
                    viewModel.applySearch(searchMode[position], key?.toString())
                }
            }
        }

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createTaskFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //handle item clicks
        return when (item.itemId) {
            R.id.item_filter_none -> {
                viewModel.applyFilter(HomeViewModel.FilterMode.NONE)
                true
            }
            R.id.item_filter_todo -> {
                viewModel.applyFilter(HomeViewModel.FilterMode.TODO)
                true
            }
            R.id.item_filter_doing -> {
                viewModel.applyFilter(HomeViewModel.FilterMode.DOING)
                true
            }
            R.id.item_filter_done -> {
                viewModel.applyFilter(HomeViewModel.FilterMode.DONE)
                true
            }
            R.id.item_sort_due_date -> {
                viewModel.applySort(HomeViewModel.SortMode.DUE)
                true
            }
            R.id.item_sort_title -> {
                viewModel.applySort(HomeViewModel.SortMode.TITLE)
                true
            }
            else -> true
        }
//        return NavigationUI.onNavDestinationSelected(
//            item,
//            requireView().findNavController()
//        ) || super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        taskAdapter =
            TaskAdapter(
                activity,
                data,
                //tagsData,
                ::updateTaskStatus,
                ::removeTask,
                ::getTagById,
                //::setTag,
                ::navigateToItemDetail
            )

        taskListRecyclerView.apply {
            layoutManager = layoutM
            adapter = taskAdapter
        }
        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            data.clear()
            data.addAll(it)
            taskAdapter.notifyDataSetChanged()
        })
        viewModel.tags.observe(viewLifecycleOwner, Observer {
            tagsData.clear()
            tagsData.addAll(it)
        })
    }

    class TaskAdapter(
        private val context: Context?,
        private val data: List<Task>,
        //private val tagsData: List<Tag>,
        private val updateTaskStatus: (Int) -> Unit,
        private val removeTask: (Int) -> Unit,
        private val getTag: (String) -> Tag?,
        //private val setTag: (String, String) -> Unit,
        private val navDetail: (String) -> Unit
    ) :
        RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

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
            var tags = data[position].tagIds.map { id -> getTag(id) }
            tags = tags.filterNotNull()
            val tagNames = tags.map { tag -> tag.name }
            val tagColors = tags.map { tag ->
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
            holder.v.task_tags_container.setTags(tagNames, tagColors)
            holder.v.setOnCreateContextMenuListener { contextMenu, _, _ ->
                contextMenu.apply {
//                    add("Set Tag").setOnMenuItemClickListener {
//                        val v =
//                            LayoutInflater.from(context).inflate(R.layout.dialog_select_tag, null)
//
//                        val tagsContainer: TagContainerLayout = v.select_tag_container
//                        tagsContainer.backgroundColor = Color.WHITE
//
//                        val tagNames = tagsData.map { tag -> tag.name }
//                        val tagColors = tagsData.map { tag ->
//                            when (tag.color) {
//                                Tag.Color.RED -> intArrayOf(
//                                    0xffff5131.toInt(),
//                                    Color.BLACK,
//                                    Color.BLACK,
//                                    Color.YELLOW
//                                )
//                                Tag.Color.BLUE -> intArrayOf(
//                                    0xff768fff.toInt(),
//                                    Color.BLACK,
//                                    Color.BLACK,
//                                    Color.YELLOW
//                                )
//                                Tag.Color.GREEN -> intArrayOf(
//                                    0xff5efc82.toInt(),
//                                    Color.BLACK,
//                                    Color.BLACK,
//                                    Color.YELLOW
//                                )
//                                else -> intArrayOf(
//                                    0xffaeaeae.toInt(),
//                                    Color.BLACK,
//                                    Color.BLACK,
//                                    Color.YELLOW
//                                )
//                            }
//                        }
//
//                        tagsContainer.setTags(tagNames, tagColors)
//
//                        var tagPos = 0
//                        var selectedTag: TagView? = null
//                        tagsContainer.setOnTagClickListener(object : TagView.OnTagClickListener {
//                            override fun onSelectedTagDrag(position: Int, text: String?) {
//                            }
//
//                            override fun onTagLongClick(position: Int, text: String?) {
//                            }
//
//                            override fun onTagClick(position: Int, text: String?) {
//                                selectedTag?.setTagBorderColor(
//                                    Color.BLACK
//                                )
//                                selectedTag = tagsContainer.getTagView(position)
//                                tagPos = position
//                                selectedTag?.setTagBorderColor(
//                                    Color.YELLOW
//                                )
//                            }
//
//                            override fun onTagCrossClick(position: Int) {
//                            }
//                        })
//                        AlertDialog.Builder(context).apply {
//                            setView(v)
//                            setTitle("Select Tag")
//                            setPositiveButton("Set") { _, _ ->
//                                setTag(data[position].id, tagsData[tagPos].id)
//                            }
//                            setNegativeButton("Cancel") { dialog, _ ->
//                                dialog.cancel()
//                            }
//                            create()
//                        }.show()
//
//                        true
//                    }
                    add("Detail").setOnMenuItemClickListener {
                        navDetail(data[position].id)
                        true
                    }
                    add("Remove").setOnMenuItemClickListener {
                        removeTask(position)
                        true
                    }
                }
            }
        }

        class TaskViewHolder(val v: View) : RecyclerView.ViewHolder(v)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment()
    }
}
