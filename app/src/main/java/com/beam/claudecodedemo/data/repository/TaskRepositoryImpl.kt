package com.beam.claudecodedemo.data.repository

import com.beam.claudecodedemo.data.local.TaskDao
import com.beam.claudecodedemo.data.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAllTasksStream(): Flow<List<Task>> = taskDao.getAllTasksStream()

    override suspend fun getTaskById(id: Long): Task? = taskDao.getTaskById(id)

    override suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)

    override suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
}
