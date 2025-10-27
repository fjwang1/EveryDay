# 开发指南

## 快速开始

### 1. 克隆项目
```bash
git clone <repository-url>
cd EveryDay
```

### 2. 导入Android Studio
1. 打开Android Studio
2. 选择 "Open an Existing Project"
3. 选择项目根目录
4. 等待Gradle同步完成

### 3. 运行项目
1. 连接Android设备或启动模拟器
2. 点击运行按钮或使用快捷键 Shift+F10
3. 首次运行会请求通知权限

## 代码规范

### Kotlin编码风格
- 使用4个空格缩进
- 类名使用帕斯卡命名法 (PascalCase)
- 函数和变量使用驼峰命名法 (camelCase)
- 常量使用全大写+下划线 (UPPER_SNAKE_CASE)

### Compose编码规范
- Composable函数名使用帕斯卡命名法
- 参数顺序：必需参数 -> 可选参数 -> Modifier -> Lambda
- 使用remember和LaunchedEffect处理状态和副作用

### 文件组织
```kotlin
// 1. Package声明
package com.wangfangjia.everyday.ui.home

// 2. Import语句（按字母顺序）
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

// 3. 类/函数定义
@Composable
fun HomeScreen() {
    // ...
}
```

## 添加新功能

### 1. 添加新的数据字段

**步骤1：** 更新数据模型
```kotlin
// data/models/DailyData.kt
data class DailyData(
    // 现有字段...
    val newField: String = ""  // 新字段
)
```

**步骤2：** 更新Repository
```kotlin
// data/repository/DailyRepository.kt
fun updateNewField(date: String, value: String) {
    // 实现更新逻辑
}
```

**步骤3：** 更新UI
```kotlin
// ui/home/components/NewSection.kt
@Composable
fun NewSection(content: String) {
    // 实现UI组件
}
```

### 2. 添加新的UI页面

**步骤1：** 创建Screen文件
```kotlin
// ui/newfeature/NewFeatureScreen.kt
@Composable
fun NewFeatureScreen(
    repository: DailyRepository,
    onNavigateBack: () -> Unit
) {
    // 实现页面
}
```

**步骤2：** 创建ViewModel
```kotlin
// ui/newfeature/NewFeatureViewModel.kt
class NewFeatureViewModel(
    private val repository: DailyRepository
) : ViewModel() {
    // 状态管理
}
```

**步骤3：** 添加到导航
```kotlin
// navigation/NavGraph.kt
object Routes {
    // 现有路由...
    const val NEW_FEATURE = "new_feature"
}

// 在NavHost中添加
composable(Routes.NEW_FEATURE) {
    NewFeatureScreen(repository, navController::popBackStack)
}
```

### 3. 添加新的通知时间

**步骤1：** 在NotificationScheduler中添加新时间
```kotlin
fun scheduleAllReminders(context: Context) {
    // 现有提醒...
    scheduleReminder(context, 10, 0, "morning", "reminder_morning")
}
```

**步骤2：** 在ReminderWorker中处理新类型
```kotlin
val (title, message) = when (reminderType) {
    // 现有类型...
    "morning" -> Pair("早安提醒", "美好的一天开始了！")
    else -> return Result.failure()
}
```

## 调试技巧

### 1. 查看DataStore数据
```kotlin
// 在代码中打印数据
viewModelScope.launch {
    val data = repository.getDailyData(date)
    Log.d("DEBUG", "Data: $data")
}
```

### 2. 测试通知
```bash
# 通过ADB触发Worker立即执行
adb shell am broadcast -a android.intent.action.BOOT_COMPLETED
```

### 3. 清除应用数据
```bash
# 清除应用数据（包括DataStore）
adb shell pm clear com.wangfangjia.everyday
```

### 4. Compose预览
使用`@Preview`注解快速预览UI组件：
```kotlin
@Preview(showBackground = true)
@Composable
fun ReminderSectionPreview() {
    EveryDayTheme {
        ReminderSection(
            reminders = listOf("测试提醒1", "测试提醒2")
        )
    }
}
```

## 常见问题

### Q1: 构建失败，提示AAR metadata错误
**解决方案：** 检查依赖版本兼容性，确保compileSdk和依赖版本匹配。

### Q2: 通知不显示
**解决方案：**
1. 检查是否授予通知权限
2. 检查通知渠道是否正确创建
3. 查看WorkManager任务是否正常调度

### Q3: 数据保存后丢失
**解决方案：**
1. 检查DataStore写入是否成功
2. 确认日期格式正确（yyyy-MM-dd）
3. 查看应用是否被系统清理数据

### Q4: UI显示异常
**解决方案：**
1. 检查Compose状态管理
2. 确认remember和LaunchedEffect使用正确
3. 检查ViewModel中的StateFlow是否正确更新

## 性能优化

### 1. Compose性能优化
- 使用`remember`缓存计算结果
- 避免在Composable中进行耗时操作
- 合理使用`derivedStateOf`

### 2. DataStore性能优化
- 批量写入数据而非频繁单次写入
- 使用Flow收集数据变化
- 避免在主线程读写

### 3. 内存优化
- 及时取消不需要的协程
- 避免在ViewModel中持有Context引用
- 合理使用图片和资源

## 测试

### 单元测试
```kotlin
class DailyRepositoryTest {
    @Test
    fun testGetDailyData() = runTest {
        // 测试逻辑
    }
}
```

### UI测试
```kotlin
class HomeScreenTest {
    @Test
    fun testReminderDisplay() {
        composeTestRule.setContent {
            HomeScreen(/* ... */)
        }
        composeTestRule.onNodeWithText("每日提醒").assertIsDisplayed()
    }
}
```

## 发布流程

### 1. 版本号更新
在`app/build.gradle.kts`中更新：
```kotlin
defaultConfig {
    versionCode = 2
    versionName = "1.1"
}
```

### 2. 构建Release版本
```bash
./gradlew assembleRelease
```

### 3. 签名配置
在`app/build.gradle.kts`中添加签名配置：
```kotlin
signingConfigs {
    create("release") {
        storeFile = file("keystore.jks")
        storePassword = "your_password"
        keyAlias = "your_alias"
        keyPassword = "your_password"
    }
}
```

## 贡献指南

1. Fork项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交Pull Request

## 联系方式

如有问题，请提交Issue或联系开发者。

