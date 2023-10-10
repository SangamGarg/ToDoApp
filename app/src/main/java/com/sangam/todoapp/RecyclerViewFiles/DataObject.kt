package com.sangam.todoapp.RecyclerViewFiles

object DataObject {
    var listData = mutableListOf<DataClassTask>()
    fun setData(
        task: String,
        currentDate: String,
        dueDate: String,
        priority: String
    ) {
        listData.add(
            DataClassTask(task, currentDate, dueDate, priority)
        )
    }

    fun getData(): MutableList<DataClassTask> {
        return listData
    }

    fun updateDate(pos: Int, task: String, dueDate: String, priority: String) {
        listData[pos].task = task
        listData[pos].dueDate = dueDate
        listData[pos].priority = priority
    }
}