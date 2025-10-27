package com.wangfangjia.everyday.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wangfangjia.everyday.data.models.DailyData
import com.wangfangjia.everyday.data.models.TimeSlotTask
import com.wangfangjia.everyday.data.repository.DailyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 编辑页ViewModel
 */
class EditViewModel(
    private val repository: DailyRepository,
    private val initialDate: String
) : ViewModel() {
    
    // 当前编辑的日期
    private val _currentDate = MutableStateFlow(initialDate)
    val currentDate: StateFlow<String> = _currentDate.asStateFlow()
    
    // 草稿数据（编辑中的临时数据）
    private val _draftData = MutableStateFlow<DailyData?>(null)
    val draftData: StateFlow<DailyData?> = _draftData.asStateFlow()
    
    // 保存状态
    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()
    
    // 是否有未保存的更改
    private val _hasUnsavedChanges = MutableStateFlow(false)
    val hasUnsavedChanges: StateFlow<Boolean> = _hasUnsavedChanges.asStateFlow()
    
    init {
        loadData()
    }
    
    /**
     * 加载数据
     */
    fun loadData() {
        viewModelScope.launch {
            try {
                val data = repository.getDailyData(_currentDate.value)
                _draftData.value = data
                _hasUnsavedChanges.value = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * 更新提醒列表
     */
    fun updateReminders(reminders: List<String>) {
        _draftData.value?.let { data ->
            _draftData.value = data.copy(reminders = reminders)
            _hasUnsavedChanges.value = true
        }
    }
    
    /**
     * 更新时间段任务
     */
    fun updateTimeSlotTask(updatedTask: TimeSlotTask) {
        _draftData.value?.let { data ->
            val updatedTasks = data.timeSlotTasks.map { task ->
                if (task.timeSlotId == updatedTask.timeSlotId) {
                    updatedTask
                } else {
                    task
                }
            }
            _draftData.value = data.copy(timeSlotTasks = updatedTasks)
            _hasUnsavedChanges.value = true
        }
    }
    
    /**
     * 更新快乐日历
     */
    fun updateHappyCalendar(content: String) {
        _draftData.value?.let { data ->
            _draftData.value = data.copy(happyCalendar = content)
            _hasUnsavedChanges.value = true
        }
    }
    
    /**
     * 更新日记
     */
    fun updateDiary(content: String) {
        _draftData.value?.let { data ->
            _draftData.value = data.copy(diary = content)
            _hasUnsavedChanges.value = true
        }
    }
    
    /**
     * 保存数据
     */
    fun saveData(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                _draftData.value?.let { data ->
                    repository.saveDailyData(data)
                    _hasUnsavedChanges.value = false
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isSaving.value = false
            }
        }
    }
    
    /**
     * 重置数据
     */
    fun resetData() {
        loadData()
    }
    
    /**
     * 切换日期
     */
    fun changeDate(newDate: String) {
        _currentDate.value = newDate
        loadData()
    }
    
    /**
     * 格式化日期显示
     */
    fun formatDateForDisplay(date: String): String {
        return repository.formatDateForDisplay(date)
    }
}

