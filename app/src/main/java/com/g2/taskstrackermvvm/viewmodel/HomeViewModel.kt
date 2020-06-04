package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.Tag
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.model.repository.ITagRepo
import com.g2.taskstrackermvvm.model.repository.ITaskRepo

class HomeViewModel(
    private val taskRepo: ITaskRepo
    , private val tagRepo: ITagRepo
) : ViewModel() {
    private val tasksData = taskRepo.getListTask()
    val tags = tagRepo.getTagsList()
    val tasks: MediatorLiveData<List<Task>> = MediatorLiveData()

    enum class FilterMode {
        TODO {
            override fun toTaskStatus(): Task.Status? = Task.Status.Todo
        },
        DOING {
            override fun toTaskStatus(): Task.Status? = Task.Status.Doing
        },
        DONE {
            override fun toTaskStatus(): Task.Status? = Task.Status.Done
        },
        NONE {
            override fun toTaskStatus(): Task.Status? = null
        };

        abstract fun toTaskStatus(): Task.Status?
    }

    enum class SortMode {
        TITLE,
        DUE
    }

    enum class SearchMode {
        TITLE {
            override fun toString(): String {
                return "Title"
            }
        },
        TAG {
            override fun toString(): String {
                return "Tag"
            }
        }
    }

    private var currentFilter = FilterMode.NONE
    private var currentSort = SortMode.DUE
    private var currentSearch = SearchMode.TITLE
    private var currentSearchKey: String? = null

    init {
        tasks.addSource(tasksData) { result: List<Task>? ->
            result?.let {
                tasks.value =
                    search(
                        sort(filter(it, currentFilter), currentSort),
                        currentSearch,
                        currentSearchKey
                    )
            }
        }
    }

    private fun filter(data: List<Task>, FilterMode: FilterMode): List<Task> {
        return data.filter { task ->
            if (FilterMode == HomeViewModel.FilterMode.NONE) true else (task.status == FilterMode.toTaskStatus())
        }
    }

    private fun sort(data: List<Task>, sortMode: SortMode): List<Task> {
        return when (sortMode) {
            SortMode.TITLE -> data.sortedWith(compareBy(Task::title))
            SortMode.DUE -> data.sortedWith(compareBy(Task::dueDate))
        }
    }

    private fun search(data: List<Task>, searchMode: SearchMode, key: String?): List<Task> {
        if (key != null && key.isNotEmpty()) {
            return data.filter { task ->
                when (searchMode) {
                    SearchMode.TITLE -> {
                        task.title.contains(key, true)
                    }
                    SearchMode.TAG -> {
                        task.tagIds.map {
                            getTagById(it)?.name
                        }.any {
                            it?.contains(key, false) ?: false
                        }
                    }
                }
            }
        } else {
            return data
        }
    }

    fun applyFilter(FilterMode: FilterMode) = tasksData.value?.let {
        tasks.value =
            search(sort(filter(it, FilterMode), currentSort), currentSearch, currentSearchKey)
    }.also {
        currentFilter = FilterMode
    }

    fun applySort(sortMode: SortMode) = tasksData.value?.let {
        tasks.value =
            search(sort(filter(it, currentFilter), sortMode), currentSearch, currentSearchKey)
    }.also {
        currentSort = sortMode
    }

    fun applySearch(searchMode: SearchMode, key: String?) = tasksData.value?.let {
        tasks.value = search(sort(filter(it, currentFilter), currentSort), searchMode, key)
    }.also {
        currentSearch = searchMode
        currentSearchKey = key
    }

    fun updateTask(task: Task) =
        taskRepo.updateTask(task)

    fun removeTask(task: Task) = taskRepo.removeTask(task)

    fun getTagById(id: String): Tag? {
        return tagRepo.getTagById(id)
    }

    fun setTag(taskId: String, tagId: String) {
        taskRepo.setTag(taskId, tagId)
    }
}