package com.wangfangjia.everyday.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wangfangjia.everyday.data.models.DailyData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "everyday_data")

/**
 * DataStore管理器，用于存储和读取每日数据
 */
class DataStoreManager(private val context: Context) {
    private val gson = Gson()
    
    companion object {
        private const val DAILY_DATA_PREFIX = "daily_data_"
        private const val ALL_DATES_KEY = "all_dates"
    }
    
    /**
     * 保存每日数据
     */
    suspend fun saveDailyData(dailyData: DailyData) {
        val key = stringPreferencesKey("$DAILY_DATA_PREFIX${dailyData.date}")
        val jsonData = gson.toJson(dailyData)
        
        context.dataStore.edit { preferences ->
            preferences[key] = jsonData
            
            // 更新日期列表
            val allDatesKey = stringPreferencesKey(ALL_DATES_KEY)
            val existingDates = preferences[allDatesKey]?.split(",")?.toMutableSet() ?: mutableSetOf()
            existingDates.add(dailyData.date)
            preferences[allDatesKey] = existingDates.joinToString(",")
        }
    }
    
    /**
     * 获取指定日期的数据
     */
    suspend fun getDailyData(date: String): DailyData? {
        val key = stringPreferencesKey("$DAILY_DATA_PREFIX$date")
        val preferences = context.dataStore.data.first()
        val jsonData = preferences[key] ?: return null
        
        return try {
            gson.fromJson(jsonData, DailyData::class.java)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * 获取所有有数据的日期列表
     */
    suspend fun getAllDates(): List<String> {
        val allDatesKey = stringPreferencesKey(ALL_DATES_KEY)
        val preferences = context.dataStore.data.first()
        val datesString = preferences[allDatesKey] ?: return emptyList()
        return datesString.split(",").filter { it.isNotEmpty() }.sorted()
    }
    
    /**
     * 删除指定日期的数据
     */
    suspend fun deleteDailyData(date: String) {
        val key = stringPreferencesKey("$DAILY_DATA_PREFIX$date")
        
        context.dataStore.edit { preferences ->
            preferences.remove(key)
            
            // 更新日期列表
            val allDatesKey = stringPreferencesKey(ALL_DATES_KEY)
            val existingDates = preferences[allDatesKey]?.split(",")?.toMutableSet() ?: mutableSetOf()
            existingDates.remove(date)
            preferences[allDatesKey] = existingDates.joinToString(",")
        }
    }
}

