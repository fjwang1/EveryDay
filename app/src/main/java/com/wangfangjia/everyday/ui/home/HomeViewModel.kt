package com.wangfangjia.everyday.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wangfangjia.everyday.data.models.DailyData
import com.wangfangjia.everyday.data.repository.DailyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 首页ViewModel
 */
class HomeViewModel(
    private val repository: DailyRepository
) : ViewModel() {
    
    // 当前选中的日期
    private val _currentDate = MutableStateFlow(repository.getTodayDate())
    val currentDate: StateFlow<String> = _currentDate.asStateFlow()
    
    // 当前日期的数据
    private val _dailyData = MutableStateFlow<DailyData?>(null)
    val dailyData: StateFlow<DailyData?> = _dailyData.asStateFlow()
    
    // 所有有数据的日期
    private val _datesWithData = MutableStateFlow<List<String>>(emptyList())
    val datesWithData: StateFlow<List<String>> = _datesWithData.asStateFlow()
    
    // 加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadData()
    }
    
    /**
     * 加载当前日期的数据
     */
    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val data = repository.getDailyData(_currentDate.value)
                _dailyData.value = data
                
                // 加载所有有数据的日期
                _datesWithData.value = repository.getAllDates()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 切换日期
     */
    fun changeDate(newDate: String) {
        _currentDate.value = newDate
        loadData()
    }
    
    /**
     * 更新任务完成状态
     */
    fun updateTaskCompletion(timeSlotId: String, isCompleted: Boolean) {
        viewModelScope.launch {
            try {
                repository.updateTaskCompletion(_currentDate.value, timeSlotId, isCompleted)
                loadData() // 重新加载数据以反映更改
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * 格式化日期显示
     */
    fun formatDateForDisplay(date: String): String {
        return repository.formatDateForDisplay(date)
    }
}

