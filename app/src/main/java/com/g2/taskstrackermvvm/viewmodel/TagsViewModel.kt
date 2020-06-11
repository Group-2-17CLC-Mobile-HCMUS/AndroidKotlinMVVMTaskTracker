package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.Tag
import com.g2.taskstrackermvvm.model.repository.ITagRepo

class TagsViewModel(private val tagsRepo: ITagRepo) : ViewModel() {
    private val mTags = tagsRepo.getTagsList()
    val tags: MediatorLiveData<List<Tag>> = MediatorLiveData()
    private var currentKey: String? = null
    private var currentMode: FilterMode = FilterMode.NONE

    init {
        tags.addSource(mTags) { result: List<Tag>? ->
            result?.let {
                tags.value = result
            }
        }
    }

    fun applyFilter(mode: FilterMode) = mTags.value?.let { data ->
        tags.value = search(filter(data, mode), currentKey)
    }.also {
        currentMode = mode
    }

    fun applySearch(key: String?) = mTags.value?.let { data ->
        tags.value = search(filter(data, currentMode), key)
    }.also {
        currentKey = key
    }

    private fun filter(data: List<Tag>, mode: FilterMode): List<Tag> {
        return data.filter { tag ->
            if (mode == FilterMode.NONE) true else tag.color == mode.toColor()
        }
    }

    private fun search(data: List<Tag>, key: String?): List<Tag> {
        return if (key != null && key.isNotEmpty()) {
            data.filter { tag ->
                tag.name.contains(key, false)
            }
        } else data
    }

    fun removeTag(tag: Tag) {
        tagsRepo.deleteTag(tag)
    }

    fun addTag(name: String, color: Tag.Color) {
        tagsRepo.createTag(name, color)
    }

    fun updateTag(tag: Tag) = tagsRepo.updateTag(tag)

    enum class FilterMode {
        NONE {
            override fun toColor(): Tag.Color? = null
        },
        RED {
            override fun toColor(): Tag.Color? = Tag.Color.RED
        },
        GREEN {
            override fun toColor(): Tag.Color? = Tag.Color.GREEN
        },
        BLUE {
            override fun toColor(): Tag.Color? = Tag.Color.BLUE
        };

        abstract fun toColor(): Tag.Color?
    }
}