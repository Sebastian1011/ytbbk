#!/bin/bash

echo "初始化Gradle wrapper..."

# 创建必要的目录
mkdir -p gradle/wrapper

# 下载gradle-wrapper.jar
echo "下载gradle-wrapper.jar..."
if command -v curl >/dev/null 2>&1; then
    curl -L -o gradle/wrapper/gradle-wrapper.jar https://repo1.maven.org/maven2/org/gradle/gradle-wrapper/8.2/gradle-wrapper-8.2.jar
elif command -v wget >/dev/null 2>&1; then
    wget -O gradle/wrapper/gradle-wrapper.jar https://repo1.maven.org/maven2/org/gradle/gradle-wrapper/8.2/gradle-wrapper-8.2.jar
else
    echo "错误: 需要curl或wget来下载文件"
    exit 1
fi

# 检查文件是否下载成功
if [ -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "Gradle wrapper下载成功!"
    ls -la gradle/wrapper/gradle-wrapper.jar
    
    # 设置执行权限
    chmod +x gradlew
    chmod +x gradlew.bat
    
    echo "现在可以运行: ./gradlew --version"
else
    echo "错误: Gradle wrapper下载失败"
    exit 1
fi
