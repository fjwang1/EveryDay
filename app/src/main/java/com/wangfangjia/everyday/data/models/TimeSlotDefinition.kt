package com.wangfangjia.everyday.data.models

/**
 * 时间段定义
 */
data class TimeSlotDefinition(
    val id: String,
    val startTime: String,
    val endTime: String,
    val displayText: String
)

/**
 * 默认时间段列表
 */
object DefaultTimeSlots {
    val slots = listOf(
        TimeSlotDefinition("slot_8_9_30", "8:00", "9:30", "8:00～9:30"),
        TimeSlotDefinition("slot_10_11", "10:00", "11:00", "10:00～11:00"),
        TimeSlotDefinition("slot_14_15", "14:00", "15:00", "2:00～3:00"),
        TimeSlotDefinition("slot_15_16", "15:00", "16:00", "3:00～4:00"),
        TimeSlotDefinition("slot_16_17", "16:00", "17:00", "4:00～5:00"),
        TimeSlotDefinition("slot_17_18", "17:00", "18:00", "5:00～6:00"),
        TimeSlotDefinition("slot_19_20_30", "19:00", "20:30", "7:00～8:30")
    )
}

