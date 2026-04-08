package com.beam.claudecodedemo.ui.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beam.claudecodedemo.data.model.Task
import com.beam.claudecodedemo.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val uiState: StateFlow<TaskListUiState> = repository
        .getAllTasksStream()
        .map { tasks -> TaskListUiState(tasks = tasks, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TaskListUiState()
        )

    fun deleteTask(task: Task) {
        viewModelScope.launch { repository.deleteTask(task) }
    }

    fun toggleDone(task: Task) {
        viewModelScope.launch {
            repository.updateTask(
                task.copy(
                    isDone = !task.isDone,
                    modifiedAt = System.currentTimeMillis()
                )
            )
        }
    }
}
