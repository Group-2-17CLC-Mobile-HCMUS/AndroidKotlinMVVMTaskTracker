package com.g2.taskstracker.model

data class User(val name: String, val uid: String) {
    private var mPoints: Int = 0

    val points
        get() = mPoints

    fun addPoint(bonus: Int) {
        mPoints += bonus
        checkAchievements()
    }

    private fun checkAchievements(): Nothing = TODO()

    private val mTasks = mutableListOf<Task>()

    val tasks: List<Task>
        get() = mTasks

    fun addTask(task: Task) = mTasks.add(task)
    fun removeTask(task: Task) = mTasks.remove(task)

    private val mTags = mutableListOf<Tag>()

    val tags: List<Tag>
        get() = mTags

    fun addTag(tag: Tag) = mTags.add(tag)
    fun removeTag(tag: Tag) = mTags.remove(tag)

}