package com.wangfangjia.everyday.notification

import android.content.Context
import androidx.work.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

/**
 * 通知调度器
 */
object NotificationScheduler {
    
    private const val WORK_TAG_NOON = "reminder_noon"
    private const val WORK_TAG_EVENING = "reminder_evening"
    private const val WORK_TAG_NIGHT = "reminder_night"
    
    /**
     * 调度所有提醒通知
     */
    fun scheduleAllReminders(context: Context) {
        scheduleReminder(context, 13, 0, ReminderWorker.REMINDER_TYPE_NOON, WORK_TAG_NOON)
        scheduleReminder(context, 19, 0, ReminderWorker.REMINDER_TYPE_EVENING, WORK_TAG_EVENING)
        scheduleReminder(context, 23, 0, ReminderWorker.REMINDER_TYPE_NIGHT, WORK_TAG_NIGHT)
    }
    
    /**
     * 调度单个提醒
     */
    private fun scheduleReminder(
        context: Context,
        hour: Int,
        minute: Int,
        reminderType: String,
        workTag: String
    ) {
        val now = LocalDateTime.now()
        val targetTime = LocalTime.of(hour, minute)
        var nextReminderTime = now.with(targetTime)
        
        // 如果今天的时间已经过了，则设置为明天
        if (now.toLocalTime().isAfter(targetTime)) {
            nextReminderTime = nextReminderTime.plusDays(1)
        }
        
        val delay = Duration.between(now, nextReminderTime).toMillis()
        
        // 创建输入数据
        val inputData = workDataOf(
            ReminderWorker.KEY_REMINDER_TYPE to reminderType
        )
        
        // 创建定期工作请求（每日重复）
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(workTag)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .build()
            )
            .build()
        
        // 调度工作
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                workTag,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }
    
    /**
     * 取消所有提醒
     */
    fun cancelAllReminders(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG_NOON)
        WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG_EVENING)
        WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG_NIGHT)
    }
}

