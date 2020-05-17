package com.g2.taskstrackermvvm.model.repository

import android.util.Log
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
}

class TagRepositoryImp : ITagRepo {
    override fun createTag(name: String, color: Tag.Color) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        assert(currentUser != null)
        val tagsRef = FirebaseDatabase.getInstance().getReference("tags/${currentUser!!.uid}")
        val tagRef = tagsRef.push()
        tagRef.setValue(tagRef.key?.let { Tag(it, name, color, null) })
    }
}