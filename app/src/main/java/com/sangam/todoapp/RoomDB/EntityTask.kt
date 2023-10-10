package com.sangam.todoapp.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Task")
data class EntityTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var task: String,
    var currentDate: String,
    var dueDate: String,
    var priority: String
)
