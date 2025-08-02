# ytbbk

一个Android应用，支持YouTube后台播放功能。

## 功能特性

- 🎵 YouTube后台播放
- 📱 基于WebView的YouTube移动端界面
- 🔄 支持前台/后台服务切换
- 🎛️ 媒体控制通知栏

## 系统要求

- **最低Android版本**: Android 7.0 (API 24)
- **目标Android版本**: Android 15 (API 35)
- **编译SDK版本**: 35

## 依赖及版本

### 构建工具
- **Gradle**: 8.2
- **Android Gradle Plugin**: 8.2.0
- **Kotlin**: 1.9.10

### 主要依赖库
- **androidx.core:core-ktx**: 1.12.0
- **androidx.appcompat:appcompat**: 1.6.1
- **com.google.android.material:material**: 1.11.0
- **androidx.constraintlayout:constraintlayout**: 2.1.4
- **androidx.webkit:webkit**: 1.8.0
- **androidx.lifecycle:lifecycle-service**: 2.7.0
- **androidx.media:media**: 1.7.0

### 测试依赖
- **JUnit**: 4.13.2
- **Mockito**: 5.1.1
- **Robolectric**: 4.11.1
- **Espresso**: 3.5.1
- **AndroidX Test**: 1.5.x

## 开发环境配置

1. **安装Android Studio**
   - 推荐使用Android Studio Flamingo或更高版本

2. **配置SDK**
   - Android SDK 35
   - Android Build Tools 35.0.0

3. **JDK版本**
   - Java 8 (1.8) 或更高版本

## 测试命令

### 单元测试
```bash
# 运行所有单元测试
./gradlew test

# 运行Debug版本单元测试
./gradlew testDebugUnitTest

# 运行Release版本单元测试
./gradlew testReleaseUnitTest

# 生成测试覆盖率报告
./gradlew testDebugUnitTestCoverage
```

### 集成测试（需要连接设备或模拟器）
```bash
# 运行所有集成测试
./gradlew connectedAndroidTest

# 运行Debug版本集成测试
./gradlew connectedDebugAndroidTest
```

### 查看测试报告
测试报告位置：
- 单元测试报告: `app/build/reports/tests/`
- 集成测试报告: `app/build/reports/androidTests/`
- 覆盖率报告: `app/build/reports/coverage/`

## 构建APK包方法

### 构建Debug版本
```bash
# 构建Debug APK
./gradlew assembleDebug

# APK输出位置
# app/build/outputs/apk/debug/ytbbk-debug.apk
```

### 构建Release版本
```bash
# 构建Release APK
./gradlew assembleRelease

# APK输出位置
# app/build/outputs/apk/release/ytbbk-release.apk
```

### 构建所有版本
```bash
# 同时构建Debug和Release版本
./gradlew assemble
```

### 清理构建
```bash
# 清理构建文件
./gradlew clean

# 清理后重新构建
./gradlew clean assembleDebug
```

## 安装和运行

### 直接安装到设备
```bash
# 安装Debug版本到连接的设备
./gradlew installDebug

# 安装Release版本到连接的设备
./gradlew installRelease
```

### 手动安装APK
```bash
# 使用adb安装APK文件
adb install app/build/outputs/apk/debug/ytbbk-debug.apk
```

## 项目结构

```
ytbbk/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/ytbbk/
│   │   │   │   ├── MainActivity.kt        # 主活动
│   │   │   │   └── service/               # 后台服务
│   │   │   ├── res/                       # 资源文件
│   │   │   └── AndroidManifest.xml       # 应用清单
│   │   ├── test/                          # 单元测试
│   │   └── androidTest/                   # 集成测试
│   └── build.gradle                       # 应用级构建配置
├── gradle/                                # Gradle Wrapper
├── build.gradle                           # 项目级构建配置
└── settings.gradle                        # 项目设置
```

## 许可证

此项目基于 MIT 许可证开源 - 查看 [LICENSE](LICENSE) 文件了解详情。
