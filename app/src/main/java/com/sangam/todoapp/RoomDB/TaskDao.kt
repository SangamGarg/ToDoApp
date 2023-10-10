package com.sangam.todoapp.RoomDB

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(entity: EntityTask)

    @Update
    suspend fun updateTask(entity: EntityTask)

    @Delete
    suspend fun deleteTask(entity: EntityTask)

    @Query("DELETE FROM Task")
    fun deleteAll()

    @Query("SELECT * FROM Task")
    fun getTask(): List<EntityTask>
}