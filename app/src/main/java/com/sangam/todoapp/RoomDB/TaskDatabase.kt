package com.sangam.todoapp.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EntityTask::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskdao(): TaskDao

    companion object {
        @Volatile  //jaise hi instance wale variable pe kuch bhi update hota h to saare thread to value pata chljaati h ki uski updated value ye h

        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(
                            context.applicationContext,
                            TaskDatabase::class.java,
                            "taskDB"
                        )
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}