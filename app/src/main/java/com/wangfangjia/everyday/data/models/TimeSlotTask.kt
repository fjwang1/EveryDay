package com.wangfangjia.everyday.data.models

/**
 * 时间段任务模型
 */
data class TimeSlotTask(
    val timeSlotId: String, // 如：slot_8_9_30
    val startTime: String, // 如：8:00
    val endTime: String, // 如：9:30
    val taskDetail: String = "",
    val isCompleted: Boolean = false,
    val note: String = ""
)

