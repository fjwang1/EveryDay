package com.wangfangjia.everyday.data.models

/**
 * 每日数据模型
 */
data class DailyData(
    val date: String, // 格式：yyyy-MM-dd
    val reminders: List<String> = emptyList(),
    val timeSlotTasks: List<TimeSlotTask> = emptyList(),
    val happyCalendar: String = "",
    val diary: String = ""
)

