package com.g2.taskstrackermvvm.model

import com.google.firebase.database.Exclude

data class Tag constructor(
    @get:Exclude var id: String,
    var name: String,
    var color: Color,
    @get:Exclude val bindedTaskId: MutableList<String>?
) {

    constructor() : this("", "", Color.RED, mutableListOf())

    fun addBindedTask(taskId: String) {
        bindedTaskId?.add(taskId)
    }

    enum class Color(val rbg: Int) {
        RED(0xFF0000),
        GREEN(0x00FF00),
        BLUE(0x0000FF)
    }
}