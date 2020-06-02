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

    private var currentFilter = FilterMode.NONE
    private var currentSort = SortMode.DUE

    init {
        tasks.addSource(tasksData) { result: List<Task>? ->
            result?.let {
                tasks.value = sort(filter(it, currentFilter), currentSort)
            }
        }
    }

    private fun filter(data: List<Task>, FilterMode: FilterMode): List<Task> {
        return data.filter { task ->
            if (FilterMode == HomeViewModel.FilterMode.NONE) true else (task.status == FilterMode.toTaskStatus())
        }
    }

    private fun sort(data: List<Task>, sortMode: SortMode): List<Task> {
        return data.sortedWith(compareBy(Task::title))
    }

    fun applyFilter(FilterMode: FilterMode) = tasksData.value?.let {
        tasks.value = sort(filter(it, FilterMode), currentSort)
    }.also {
        currentFilter = FilterMode
    }

    fun applySort(sortMode: SortMode) = tasksData.value?.let {
        tasks.value = sort(filter(it, currentFilter), sortMode)
    }.also {
        currentSort = sortMode
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