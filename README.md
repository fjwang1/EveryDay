## EveryDay Android 项目架构与目录说明

本项目是一个使用 Kotlin 与 Jetpack Compose 构建的每日任务规划与进度跟踪应用，采用 MVVM + Repository 的清晰分层与现代 Android 构建体系（Gradle、Version Catalog、Gradle Wrapper）。本文档聚焦架构与目录/文件说明，帮助快速理解与维护项目。

---

### 项目概览
- **单模块结构**：仅包含 `app` 模块，便于轻量维护与快速迭代。
- **核心技术**：Jetpack Compose（UI）、Material3（设计体系）、DataStore（本地持久化）、Navigation Compose（导航）、WorkManager（后台任务）、Coroutines/Flow（异步）。
- **目标系统与工具链**：
  - Android Gradle Plugin（AGP）: 8.9.1（由版本目录管理）
  - Kotlin: 2.0.21
  - Gradle Wrapper: 8.11.1（`gradle/wrapper/gradle-wrapper.properties`）
  - compileSdk: 35 / targetSdk: 35 / minSdk: 24
  - JDK: 11

---

## 顶层目录与构建体系

- `build.gradle.kts`
  - 项目级构建脚本（顶层）。仅声明插件别名并 `apply false`，具体启用在 `app/build.gradle.kts`。
  - 依赖与插件版本通过 Version Catalog 管理，提升一致性与可维护性。

- `settings.gradle.kts`
  - 仓库源配置（`pluginManagement` 与 `dependencyResolutionManagement`）。
  - `rootProject.name = "EveryDay"` 指定工程名称。
  - `include(":app")` 声明唯一模块。

- `gradle/libs.versions.toml`
  - **Version Catalog**：集中管理版本与库坐标。
  - 关键版本：
    - `agp = "8.9.1"`
    - `kotlin = "2.0.21"`
    - `composeBom = "2024.09.00"`
    - Jetpack 组件版本（Activity Compose、Lifecycle、Navigation、WorkManager、DataStore 等）
  - 关键库别名：
    - `androidx-compose-bom`、`androidx-material3`、`androidx-navigation-compose`、`androidx-work-runtime-ktx`、`androidx-datastore-preferences`、`gson` 等
  - 关键插件别名：
    - `android-application`、`kotlin-android`、`kotlin-compose`

- `gradle.properties`
  - 全局 Gradle 行为与 Android 构建参数：
    - `org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8`
    - `android.useAndroidX=true`
    - `kotlin.code.style=official`
    - `android.nonTransitiveRClass=true`（更小的 R 类）

- `gradle/wrapper/gradle-wrapper.properties`
  - Gradle Wrapper 配置，指向 `gradle-8.11.1-bin.zip`。
  - 搭配 `./gradlew` / `./gradlew.bat` 跨平台一致构建。

- 文档
  - `README.md`（本文档）：架构与目录说明。
  - `DEVELOPMENT_GUIDE.md`：开发规范与快速开始。
  - `BUILD_RELEASE.md`：发布与签名、ProGuard/R8 配置建议。
  - `PROJECT_SUMMARY.md`：项目实施总结与完成项清单。

---

## 模块结构（app）

- `app/build.gradle.kts`
  - 启用插件：`com.android.application`、`org.jetbrains.kotlin.android`、`org.jetbrains.kotlin.plugin.compose`。
  - Android 配置：
    - `namespace = "com.wangfangjia.everyday"`
    - `compileSdk = 35`
    - `defaultConfig { applicationId = "com.wangfangjia.everyday"; minSdk = 24; targetSdk = 35; versionCode = 1; versionName = "1.0" }`
    - `buildFeatures { compose = true }`
    - `compileOptions`/`kotlinOptions`（JDK/字节码目标 11）
  - 依赖（由版本目录驱动）：
    - Compose BOM + UI/Material3、Activity Compose、Lifecycle Runtime KTX、Lifecycle ViewModel Compose
    - DataStore Preferences
    - Navigation Compose
    - WorkManager KTX
    - Gson（JSON 序列化）
    - 测试依赖（JUnit、AndroidX Test、Compose UI Test）

- `app/proguard-rules.pro`
  - 模块级混淆规则文件。当前为默认模板。
  - 若启用 Release 混淆与资源压缩，建议参考 `BUILD_RELEASE.md` 增补对 Compose/DataStore/WorkManager/Gson 的保留规则。

- `app/src/main/AndroidManifest.xml`
  - `application name=".EveryDayApp"`、主题与图标、备份/数据提取规则。
  - 权限：
    - `POST_NOTIFICATIONS`（Android 13+）
    - `SCHEDULE_EXACT_ALARM`（可选，精确提醒）
  - `MainActivity` 为入口，带 `LAUNCHER` Intent 过滤。

---

## 代码与资源目录

- `app/src/main/java/com/wangfangjia/everyday/`
  - `data/`
    - `models/`：数据模型（`DailyData.kt`、`TimeSlotTask.kt`、`TimeSlotDefinition.kt`）
    - `DataStoreManager.kt`：DataStore 读写与序列化封装
    - `repository/`：仓库层（`DailyRepository.kt`），聚合数据访问与业务逻辑
  - `ui/`
    - `theme/`：主题、配色与字体（`Color.kt`、`Theme.kt`、`Type.kt`）
    - `components/`：通用组件（`CalendarDialog.kt`、`EmptyPlaceholder.kt`）
    - `home/`：首页
      - `HomeScreen.kt`、`HomeViewModel.kt`
      - `components/`：`ReminderSection.kt`、`TimeSlotSection.kt`、`HappyCalendarSection.kt`、`DiarySection.kt`
    - `edit/`：编辑页
      - `EditScreen.kt`、`EditViewModel.kt`
      - `components/`：`EditableReminderSection.kt`、`EditableTimeSlotSection.kt`、`MultiLineTextField.kt`
  - `notification/`
    - `NotificationHelper.kt`：通知渠道与展示
    - `ReminderWorker.kt`：WorkManager 后台任务
    - `NotificationScheduler.kt`：统一调度（13:00/19:00/23:00）
  - `navigation/`
    - `NavGraph.kt`：导航图与路由
  - `MainActivity.kt`：应用主入口，承载导航与根 UI
  - `EveryDayApp.kt`：`Application`，完成初始化与全局配置

- `app/src/main/res/`
  - `values/`：`colors.xml`、`themes.xml`、`strings.xml`
  - `xml/`：`backup_rules.xml`、`data_extraction_rules.xml`
  - `drawable/`：图标资源（含 `app_icon.png`）
  - `mipmap-*`：启动图标（各种密度）

- 测试目录
  - `app/src/androidTest/`：仪器测试骨架
  - `app/src/test/`：单元测试骨架

---

## 架构与数据流

- **MVVM**：`ViewModel` 暴露 `StateFlow` 状态；`Composable` 订阅渲染；用户交互触发仓库更新。
- **Repository**：封装 DataStore 存取与业务逻辑（如提醒复用、日期格式化、任务勾选状态）。
- **DataStore + Gson**：每日数据 JSON 化存储，按日期键值管理。
- **WorkManager + Notification**：按固定时间触发提醒通知；可扩展更多时间点或类型。
- **Navigation**：路由与参数（如日期）在 `NavGraph.kt` 管理。

---

## 构建与运行

- 环境要求
  - Android Studio Hedgehog (2023.1.1)+
  - JDK 11+
  - Android SDK 35
- 常用命令
  ```bash
  ./gradlew clean
  ./gradlew assembleDebug
  ./gradlew installDebug
  ```
- 发布与签名
  - 参考 `BUILD_RELEASE.md`（签名密钥、`keystore.properties`、`signingConfigs`、ProGuard/R8 建议与 `bundleRelease`/`assembleRelease` 流程）。

---

## 维护与扩展

- 开发规范与常见问题：见 `DEVELOPMENT_GUIDE.md`。
- 新功能建议路径：
  - 数据模型 → 仓库 → UI 页面/组件 → 导航/通知（如需）。
- 混淆与体积优化：
  - 启用 `isMinifyEnabled` / `isShrinkResources` 后，按需增补保留规则。
  - 使用 APK/AAB 分析工具优化资源占用。

---

## 致谢
- 作者：wangfangjia

