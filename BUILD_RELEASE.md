# 构建Release版本指南

## 准备工作

### 1. 生成签名密钥

如果还没有签名密钥，需要先生成：

```bash
keytool -genkey -v -keystore everyday-release.jks \
  -alias everyday \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

按提示输入：
- 密钥库密码
- 姓名、组织等信息
- 密钥密码

**重要：** 请妥善保管密钥文件和密码！

### 2. 配置签名信息

在项目根目录创建 `keystore.properties` 文件（不要提交到Git）：

```properties
storeFile=everyday-release.jks
storePassword=你的密钥库密码
keyAlias=everyday
keyPassword=你的密钥密码
```

### 3. 更新build.gradle.kts

在 `app/build.gradle.kts` 中添加签名配置：

```kotlin
// 在android块之前添加
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    // ... 现有配置

    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

### 4. 配置ProGuard规则

在 `app/proguard-rules.pro` 中添加必要的混淆规则：

```proguard
# Keep data classes for Gson
-keep class com.wangfangjia.everyday.data.models.** { *; }

# Keep Compose functions
-keep class androidx.compose.** { *; }

# Keep DataStore
-keep class androidx.datastore.** { *; }

# Keep WorkManager
-keep class androidx.work.** { *; }

# Gson rules
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
```

## 构建Release APK

### 方法1：使用命令行

```bash
# 清理项目
./gradlew clean

# 构建Release APK
./gradlew assembleRelease

# APK位置：
# app/build/outputs/apk/release/app-release.apk
```

### 方法2：使用Android Studio

1. 菜单：Build → Generate Signed Bundle / APK
2. 选择：APK
3. 选择密钥文件
4. 输入密码
5. 选择release构建类型
6. 点击Finish

## 构建AAB（推荐Google Play上架）

```bash
./gradlew bundleRelease

# AAB位置：
# app/build/outputs/bundle/release/app-release.aab
```

## 版本号管理

在 `app/build.gradle.kts` 中更新版本：

```kotlin
defaultConfig {
    versionCode = 1  // 每次发布递增
    versionName = "1.0.0"  // 语义化版本号
}
```

版本号规则：
- **Major.Minor.Patch**
- Major: 重大更新
- Minor: 新功能
- Patch: 修复bug

## 优化建议

### 1. 减小APK体积

```kotlin
buildTypes {
    release {
        // 启用代码混淆
        isMinifyEnabled = true
        // 启用资源压缩
        isShrinkResources = true
        // 移除无用的备选资源配置
        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
        }
    }
}
```

### 2. 启用R8优化

在 `gradle.properties` 中添加：

```properties
android.enableR8.fullMode=true
```

### 3. 分析APK大小

```bash
# 使用Android Studio的APK Analyzer
# Build → Analyze APK → 选择APK文件
```

## 测试Release版本

### 1. 安装测试

```bash
# 卸载旧版本
adb uninstall com.wangfangjia.everyday

# 安装Release APK
adb install app/build/outputs/apk/release/app-release.apk
```

### 2. 功能测试清单

- [ ] 应用启动正常
- [ ] 所有页面可以正常打开
- [ ] 数据保存和读取正常
- [ ] 通知功能正常
- [ ] 日历功能正常
- [ ] 编辑功能正常
- [ ] 深色模式正常
- [ ] 权限请求正常
- [ ] 应用退出和恢复正常

### 3. 性能测试

```bash
# 查看应用内存使用
adb shell dumpsys meminfo com.wangfangjia.everyday

# 查看CPU使用
adb shell top | grep everyday

# 查看电池使用
adb shell dumpsys batterystats
```

## 发布检查清单

### 代码检查
- [ ] 移除所有Log.d()调试日志
- [ ] 移除测试代码
- [ ] 检查代码混淆规则
- [ ] 更新版本号
- [ ] 更新changelog

### 资源检查
- [ ] 检查应用图标
- [ ] 检查启动图标
- [ ] 检查字符串资源
- [ ] 优化图片资源

### 配置检查
- [ ] 检查AndroidManifest
- [ ] 检查权限声明
- [ ] 检查签名配置
- [ ] 检查混淆规则

### 功能检查
- [ ] 完整功能测试
- [ ] 不同设备测试
- [ ] 不同Android版本测试
- [ ] 横竖屏测试
- [ ] 网络异常测试

## 上架应用商店

### Google Play

1. 创建Google Play开发者账号
2. 创建应用
3. 填写应用信息
4. 上传AAB文件
5. 设置定价和分发
6. 提交审核

### 其他应用商店

- 华为应用市场
- 小米应用商店
- OPPO软件商店
- vivo应用商店
- 应用宝（腾讯）

每个应用商店都有自己的审核标准和要求。

## 持续集成

### GitHub Actions示例

创建 `.github/workflows/release.yml`：

```yaml
name: Release APK

on:
  push:
    tags:
      - 'v*'

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'adopt'
    
    - name: Build Release APK
      run: ./gradlew assembleRelease
    
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-release
        path: app/build/outputs/apk/release/app-release.apk
```

## 常见问题

### Q1: 签名失败
**解决：** 检查密钥路径和密码是否正确

### Q2: ProGuard错误
**解决：** 添加相应的keep规则

### Q3: APK体积过大
**解决：** 启用代码混淆和资源压缩

### Q4: 安装失败
**解决：** 检查签名是否正确，卸载旧版本后重试

## 安全建议

1. **不要** 将密钥文件提交到Git
2. **不要** 在代码中硬编码密码
3. **不要** 共享密钥文件
4. **定期** 备份密钥文件
5. **使用** 密钥管理服务（如果是团队开发）

## 发布后

### 监控

- 使用Firebase Crashlytics监控崩溃
- 使用Google Analytics分析用户行为
- 收集用户反馈

### 更新

- 定期发布更新版本
- 修复用户反馈的问题
- 添加新功能

---

**提示：** 首次发布前请仔细测试所有功能！

