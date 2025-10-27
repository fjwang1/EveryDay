package com.wangfangjia.everyday.data.repository

import com.wangfangjia.everyday.data.DataStoreManager
import com.wangfangjia.everyday.data.models.DailyData
import com.wangfangjia.everyday.data.models.DefaultTimeSlots
import com.wangfangjia.everyday.data.models.TimeSlotTask
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 每日数据仓库
 */
class DailyRepository(private val dataStoreManager: DataStoreManager) {
    
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    
    /**
     * 获取指定日期的数据
     * 如果当日无提醒数据，自动复用前一日的提醒内容
     */
    suspend fun getDailyData(date: String): DailyData {
        val existingData = dataStoreManager.getDailyData(date)
        
        if (existingData != null) {
            return existingData
        }
        
        // 如果没有数据，创建一个带有默认时间段的空数据
        val defaultTimeSlotTasks = DefaultTimeSlots.slots.map { slot ->
            TimeSlotTask(
                timeSlotId = slot.id,
                startTime = slot.startTime,
                endTime = slot.endTime,
                taskDetail = "",
                isCompleted = false,
                note = ""
            )
        }
        
        // 尝试复用前一天的提醒
        val previousDayReminders = getPreviousDayReminders(date)
        
        return DailyData(
            date = date,
            reminders = previousDayReminders,
            timeSlotTasks = defaultTimeSlotTasks,
            happyCalendar = "",
            diary = ""
        )
    }
    
    /**
     * 保存每日数据
     */
    suspend fun saveDailyData(dailyData: DailyData) {
        dataStoreManager.saveDailyData(dailyData)
    }
    
    /**
     * 获取所有有数据的日期
     */
    suspend fun getAllDates(): List<String> {
        return dataStoreManager.getAllDates()
    }
    
    /**
     * 更新任务完成状态
     */
    suspend fun updateTaskCompletion(date: String, timeSlotId: String, isCompleted: Boolean) {
        val dailyData = getDailyData(date)
        val updatedTasks = dailyData.timeSlotTasks.map { task ->
            if (task.timeSlotId == timeSlotId) {
                task.copy(isCompleted = isCompleted)
            } else {
                task
            }
        }
        
        val updatedData = dailyData.copy(timeSlotTasks = updatedTasks)
        saveDailyData(updatedData)
    }
    
    /**
     * 获取前一天的提醒内容
     */
    private suspend fun getPreviousDayReminders(date: String): List<String> {
        try {
            val currentDate = LocalDate.parse(date, dateFormatter)
            val previousDate = currentDate.minusDays(1)
            val previousDateStr = previousDate.format(dateFormatter)
            
            val previousData = dataStoreManager.getDailyData(previousDateStr)
            return previousData?.reminders ?: emptyList()
        } catch (e: Exception) {
            return emptyList()
        }
    }
    
    /**
     * 获取今天的日期字符串
     */
    fun getTodayDate(): String {
        return LocalDate.now().format(dateFormatter)
    }
    
    /**
     * 格式化日期显示（如：10月27号）
     */
    fun formatDateForDisplay(date: String): String {
        return try {
            val localDate = LocalDate.parse(date, dateFormatter)
            "${localDate.monthValue}月${localDate.dayOfMonth}号"
        } catch (e: Exception) {
            date
        }
    }
}

