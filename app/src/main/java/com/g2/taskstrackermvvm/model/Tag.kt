package com.g2.taskstrackermvvm.model

data class Tag(val name: String, val color: Color, val id: Int = nextId) {
    companion object {
        private var nextId = 0
    }

    fun changName(newName: String) = Tag(newName, color, id)
    fun changeColor(newColor: Color) = Tag(name, newColor, id)

    enum class Color(val rbg: Int) {
        RED(0xFF0000),
        GREEN(0x00FF00),
        BLUE(0x0000FF)
    }
}