package com.g2.taskstrackermvvm.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.g2.taskstrackermvvm.model.Tag
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

interface ITagRepo {
    fun createTag(name: String, color: Tag.Color)
    fun deleteTag(tag: Tag)
    fun getTagsList(): LiveData<List<Tag>>
    fun updateTag(tag: Tag)
    fun getTagById(id: String): Tag?
    fun cleanUp()
}

class TagRepositoryImp : ITagRepo {
    companion object {
        val TAG = "com.g2.taskstrackermvvm.model.repository.TagRepositoryImp"
    }

    private val listTags: MutableLiveData<List<Tag>> = MutableLiveData()
    private var isListFetched = false
    private val listenerTrackMap: MutableMap<Query, ValueEventListener> = mutableMapOf()

    override fun createTag(name: String, color: Tag.Color) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val tagsRef = FirebaseDatabase.getInstance().getReference("tags/${currentUser.uid}")
            val tagRef = tagsRef.push()
            tagRef.setValue(tagRef.key?.let { Tag(it, name, color) })
        }
    }

    override fun deleteTag(tag: Tag) {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val tagsRef = database.getReference("tags/${user.uid}")
            val tasksRef = database.getReference("tasks/${user.uid}")

            for (taskId in tag.bindedTaskId) {
                tasksRef.child("$taskId/tags/${tag.id}").setValue(null)
            }

            tagsRef.child(tag.id).setValue(null)
        }

    }

    override fun getTagsList(): LiveData<List<Tag>> {
        if (isListFetched)
            return listTags
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        val list = mutableListOf<Tag>()

        if (user != null) {
            val tagRef = database.getReference("tags")
            val tagUidRef = tagRef.child(user.uid)
            val listener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    list.clear()
                    for (tagSnapshot in dataSnapshot.children) {
                        val tag = tagSnapshot.getValue(Tag::class.java)
                        if (tag != null) {
                            tag.id = tagSnapshot.key.toString()
                            for (task in tagSnapshot.child("tasks").children) {
                                task.key?.let { tag.addBindedTask(it) }
                            }
                            list.add(tag)
                        }
                    }

                    listTags.value = list
                }
            }
            listenerTrackMap[tagUidRef] = listener
            tagUidRef.addValueEventListener(listener)
            isListFetched = true
        }
        return listTags
    }

    override fun updateTag(tag: Tag) {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val tagsRef = database.getReference("tags/${user.uid}/${tag.id}")
            tagsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "loadPost:onCancelled", error.toException())
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        tagsRef.child("name").setValue(tag.name)
                        tagsRef.child("color").setValue(tag.color)
                    }
                }
            })
        }
    }

    override fun getTagById(id: String): Tag? {
        val tags = listTags.value ?: return null
        for (tag in tags) {
            if (tag.id == id)
                return tag
        }
        return null
    }

    override fun cleanUp() {
        listenerTrackMap.forEach { (query, listener) ->
            query.removeEventListener(listener)
        }
        listTags.value = listOf()
        isListFetched = false
    }
}