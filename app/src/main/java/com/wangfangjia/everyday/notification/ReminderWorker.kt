package com.wangfangjia.everyday.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * 定时提醒Worker
 */
class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            // 获取提醒类型（午间、晚间、夜间）
            val reminderType = inputData.getString(KEY_REMINDER_TYPE) ?: return Result.failure()
            
            // 发送通知
            val (title, message) = when (reminderType) {
                REMINDER_TYPE_NOON -> Pair("午间提醒", "记得更新今日任务进度哦～")
                REMINDER_TYPE_EVENING -> Pair("晚间提醒", "今天的任务完成得怎么样了？")
                REMINDER_TYPE_NIGHT -> Pair("夜间提醒", "睡前记录一下今日的收获吧～")
                else -> return Result.failure()
            }
            
            NotificationHelper.sendReminderNotification(applicationContext, title, message)
            
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
    
    companion object {
        const val KEY_REMINDER_TYPE = "reminder_type"
        const val REMINDER_TYPE_NOON = "noon"
        const val REMINDER_TYPE_EVENING = "evening"
        const val REMINDER_TYPE_NIGHT = "night"
    }
}

