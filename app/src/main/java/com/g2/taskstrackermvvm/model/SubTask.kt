package com.g2.taskstrackermvvm.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class SubTask constructor(
    @get:Exclude var id: String,
    var name: String,
    val status: Status = Status.UNFINISHED
) {

    constructor() : this("", "")

    override fun toString(): String {
        return name
    }

    enum class Status {
        UNFINISHED,
        FINISH
    }
}
