package com.wangfangjia.everyday

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.wangfangjia.everyday.data.DataStoreManager
import com.wangfangjia.everyday.data.repository.DailyRepository
import com.wangfangjia.everyday.navigation.AppNavGraph
import com.wangfangjia.everyday.ui.theme.EveryDayTheme

class MainActivity : ComponentActivity() {
    
    private lateinit var repository: DailyRepository
    
    // 请求通知权限的启动器
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // 权限结果处理（可选）
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 初始化Repository
        val dataStoreManager = DataStoreManager(applicationContext)
        repository = DailyRepository(dataStoreManager)
        
        // 请求通知权限（Android 13+）
        requestNotificationPermission()
        
        setContent {
            EveryDayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    AppNavGraph(
                        navController = navController,
                        repository = repository
                    )
                }
            }
        }
    }
    
    /**
     * 请求通知权限
     */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // 权限已授予
                }
                else -> {
                    // 请求权限
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}