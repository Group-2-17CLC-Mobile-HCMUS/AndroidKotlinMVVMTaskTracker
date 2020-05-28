package com.g2.taskstrackermvvm.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.g2.taskstrackermvvm.model.Tag
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

interface ITagRepo {
    fun createTag(name: String, color: Tag.Color)
    fun deleteTag(tag: Tag)
    fun getTagsList(): LiveData<List<Tag>>
    fun updateTag(tag: Tag)
    fun getTagById(id: String) : Tag?
}

class TagRepositoryImp : ITagRepo {
    companion object {
        val TAG = "com.g2.taskstrackermvvm.model.repository.TagRepositoryImp"
    }

    private val listTags: MutableLiveData<List<Tag>> = MutableLiveData()
    private var isListFetched = false

    override fun createTag(name: String, color: Tag.Color) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        assert(currentUser != null)
        val tagsRef = FirebaseDatabase.getInstance().getReference("tags/${currentUser!!.uid}")
        val tagRef = tagsRef.push()
        tagRef.setValue(tagRef.key?.let { Tag(it, name, color) })
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

        val tagRef = database.getReference("tags")
        val tagUidRef = user?.uid?.let { tagRef.child(it) }

        tagUidRef?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (tagSnapshot in dataSnapshot.children) {
                    val tag = tagSnapshot.getValue(Tag::class.java)
                    if (tag != null) {
                        tag.id = tagSnapshot.key.toString()
                        for (task in tagSnapshot.child("tags").children) {
                            task.key?.let { tag.addBindedTask(it) }
                        }
                        list.add(tag)
                    }
                }

                listTags.value = list
            }
        }
        )
        isListFetched = true
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

    override fun getTagById(id: String) : Tag? {
        val tags = listTags.value ?: return null
        for (tag in tags) {
            if (tag.id == id)
                return tag
        }
        return null
    }
}