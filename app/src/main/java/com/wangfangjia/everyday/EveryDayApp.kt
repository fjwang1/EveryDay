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
        
        NotificationHelper.createNotificationChannel(this)
        
        NotificationScheduler.scheduleAllReminders(this)
    }
}

