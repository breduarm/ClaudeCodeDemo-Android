package com.beam.claudecodedemo.ui.taskdetail

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beam.claudecodedemo.R
import com.beam.claudecodedemo.data.model.Task
import com.beam.claudecodedemo.data.repository.TaskRepository
import com.beam.claudecodedemo.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskDetailUiState(
    val taskId: Long = Screen.TaskDetail.NEW_TASK_ID,
    val title: String = "",
    val description: String = "",
    val isDone: Boolean = false,
    val createdAt: Long = 0L,
    val modifiedAt: Long = 0L,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    @StringRes val errorRes: Int? = null
)

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val repository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: Long = checkNotNull(savedStateHandle[Screen.TaskDetail.ARG_TASK_ID])

    private val _uiState = MutableStateFlow(TaskDetailUiState(taskId = taskId))
    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    init {
        if (taskId != Screen.TaskDetail.NEW_TASK_ID) {
            loadTask(taskId)
        }
    }

    private fun loadTask(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val task = repository.getTaskById(id)
            if (task != null) {
                _uiState.update {
                    it.copy(
                        title = task.title,
                        description = task.description,
                        isDone = task.isDone,
                        createdAt = task.createdAt,
                        modifiedAt = task.modifiedAt,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value, errorRes = null) }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value) }
    }

    fun onIsDoneChange(value: Boolean) {
        _uiState.update { it.copy(isDone = value) }
    }

    fun saveTask() {
        val state = _uiState.value
        if (state.title.isBlank()) {
            _uiState.update { it.copy(errorRes = R.string.error_title_empty) }
            return
        }
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            if (taskId == Screen.TaskDetail.NEW_TASK_ID) {
                repository.insertTask(
                    Task(
                        title = state.title.trim(),
                        description = state.description.trim(),
                        createdAt = now,
                        modifiedAt = now
                    )
                )
            } else {
                repository.updateTask(
                    Task(
                        id = taskId,
                        title = state.title.trim(),
                        description = state.description.trim(),
                        isDone = state.isDone,
                        createdAt = state.createdAt,
                        modifiedAt = now
                    )
                )
            }
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorRes = null) }
    }
}
