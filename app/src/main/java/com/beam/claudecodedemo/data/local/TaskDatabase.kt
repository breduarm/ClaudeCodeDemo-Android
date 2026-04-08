package com.beam.claudecodedemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.beam.claudecodedemo.data.model.Task

@Database(entities = [Task::class], version = 1, exportSchema = true)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
