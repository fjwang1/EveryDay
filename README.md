# 每日任务规划App

一个基于Android Jetpack Compose开发的每日任务规划和进度跟踪应用。

## 功能特性

### 核心功能
1. **首页（只读模式）**
   - 展示当日任务和完成进度
   - 支持查看任意日期的任务数据
   - 仅任务完成状态可操作

2. **编辑页**
   - 编辑当日或指定日期的任务内容
   - 支持新增、编辑、删除提醒
   - 可编辑时间段任务、快乐日历和日记

3. **日历弹窗**
   - 选择查看不同日期的任务数据
   - 灰色标记表示无数据的日期

4. **推送通知**
   - 中午13:00提醒更新任务进度
   - 下午19:00提醒更新任务进度
   - 晚上23:00提醒更新任务进度

### 数据模块

#### 每日提醒
- 支持多条提醒内容
- 当日无提醒时自动复用前一日内容
- 支持左滑删除操作

#### 时间段任务（固定时间段）
- 8:00～9:30
- 10:00～11:00
- 2:00～3:00
- 3:00～4:00
- 4:00～5:00
- 5:00～6:00
- 7:00～8:30

每个时间段包含：
- 任务详情
- 完成状态勾选框
- 备注（可选）

#### 快乐日历
- 记录今日快乐的事情
- 多行文本输入

#### 今日日记
- 记录今日的感想和总结
- 多行文本输入

## 技术架构

### 技术栈
- **Kotlin** - 主要开发语言
- **Jetpack Compose** - 声明式UI框架
- **Material Design 3** - UI设计规范
- **DataStore (Preferences)** - 本地数据持久化
- **Navigation Compose** - 导航管理
- **WorkManager** - 后台任务调度
- **Coroutines & Flow** - 异步编程

### 项目结构

```
app/src/main/java/com/wangfangjia/everyday/
├── data/                           # 数据层
│   ├── models/                     # 数据模型
│   │   ├── DailyData.kt           # 每日数据模型
│   │   ├── TimeSlotTask.kt        # 时间段任务模型
│   │   └── TimeSlotDefinition.kt  # 时间段定义
│   ├── DataStoreManager.kt        # DataStore管理器
│   └── repository/                 # 仓库层
│       └── DailyRepository.kt     # 每日数据仓库
│
├── ui/                             # UI层
│   ├── theme/                      # 主题配置
│   │   ├── Color.kt               # 颜色定义
│   │   ├── Theme.kt               # 主题配置
│   │   └── Type.kt                # 字体配置
│   │
│   ├── components/                 # 通用组件
│   │   ├── CalendarDialog.kt      # 日历弹窗
│   │   └── EmptyPlaceholder.kt    # 空状态占位符
│   │
│   ├── home/                       # 首页
│   │   ├── HomeScreen.kt          # 首页主界面
│   │   ├── HomeViewModel.kt       # 首页ViewModel
│   │   └── components/            # 首页组件
│   │       ├── ReminderSection.kt
│   │       ├── TimeSlotSection.kt
│   │       ├── HappyCalendarSection.kt
│   │       └── DiarySection.kt
│   │
│   └── edit/                       # 编辑页
│       ├── EditScreen.kt          # 编辑页主界面
│       ├── EditViewModel.kt       # 编辑页ViewModel
│       └── components/            # 编辑页组件
│           ├── EditableReminderSection.kt
│           ├── EditableTimeSlotSection.kt
│           └── MultiLineTextField.kt
│
├── notification/                   # 通知模块
│   ├── NotificationHelper.kt      # 通知辅助类
│   ├── ReminderWorker.kt          # 定时任务Worker
│   └── NotificationScheduler.kt   # 通知调度器
│
├── navigation/                     # 导航配置
│   └── NavGraph.kt                # 导航图
│
├── MainActivity.kt                 # 主Activity
└── EveryDayApp.kt                 # Application类
```

## 编译运行

### 环境要求
- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 11 或更高版本
- Android SDK 35
- Gradle 8.9.1

### 构建项目

```bash
# 清理项目
./gradlew clean

# 构建Debug版本
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug

# 运行
./gradlew app:installDebug
```

### 权限说明

应用需要以下权限：
- `POST_NOTIFICATIONS` - 发送推送通知（Android 13+）
- `SCHEDULE_EXACT_ALARM` - 精确闹钟（可选，用于精确时间提醒）

## UI设计风格

采用Material Design 3简约风格：
- 柔和清爽的配色方案
- 圆角卡片设计
- 简洁的交互体验
- 支持深色模式

### 配色方案
- **Primary（主色）**: 柔和的蓝色 (#6B9AE4)
- **Secondary（辅助色）**: 清新的绿色 (#8DBFA6)
- **Tertiary（强调色）**: 温暖的橙色 (#FFB366)

## 数据存储

### 本地存储
使用DataStore (Preferences) 存储所有数据：
- 每日数据以JSON格式序列化存储
- 支持按日期快速查询
- 自动维护日期列表

### 数据格式
```kotlin
DailyData(
    date: String,              // yyyy-MM-dd
    reminders: List<String>,   // 提醒列表
    timeSlotTasks: List<TimeSlotTask>,  // 时间段任务
    happyCalendar: String,     // 快乐日历
    diary: String              // 日记
)
```

## 未来规划

- [ ] 后端API对接
- [ ] 数据云同步
- [ ] 任务统计和分析
- [ ] 自定义时间段
- [ ] 任务标签和分类
- [ ] 导出数据功能
- [ ] 主题自定义

## 开发者

王方甲

## 许可证

版权所有 © 2025

