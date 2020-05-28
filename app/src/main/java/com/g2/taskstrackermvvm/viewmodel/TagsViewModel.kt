package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.Tag
import com.g2.taskstrackermvvm.model.repository.ITagRepo

class TagsViewModel(private val tagsRepo: ITagRepo) : ViewModel() {
    private val mTags = tagsRepo.getTagsList()
    val tags
        get() = mTags

    fun removeTag(tag: Tag) {
        tagsRepo.deleteTag(tag)
    }

    fun addTag(name: String, color: Tag.Color) {
        tagsRepo.createTag(name, color)
    }

    fun updateTag(tag: Tag) = tagsRepo.updateTag(tag)
}