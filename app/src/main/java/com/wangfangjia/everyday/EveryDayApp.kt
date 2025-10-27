package com.wangfangjia.everyday

import android.app.Application
import com.wangfangjia.everyday.notification.NotificationHelper
import com.wangfangjia.everyday.notification.NotificationScheduler

/**
 * 应用Application类
 */
class EveryDayApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // 创建通知渠道
        NotificationHelper.createNotificationChannel(this)
        
        // 调度提醒通知
        NotificationScheduler.scheduleAllReminders(this)
    }
}

