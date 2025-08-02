package com.ytbbk.service

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ServiceTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class YouTubeBackgroundServiceInstrumentedTest {

    @get:Rule
    val serviceRule = ServiceTestRule()

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testServiceStartsAndStops() {
        // 创建服务Intent
        val serviceIntent = Intent(context, YouTubeBackgroundService::class.java).apply {
            putExtra("url", "https://m.youtube.com/watch?v=test")
        }

        // 启动服务
        serviceRule.startService(serviceIntent)

        // 验证服务正在运行
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Integer.MAX_VALUE)
        
        val serviceRunning = runningServices.any { serviceInfo ->
            serviceInfo.service.className == YouTubeBackgroundService::class.java.name
        }
        
        assert(serviceRunning)
    }

    @Test
    fun testServiceWithNullIntent() {
        // 测试服务处理null intent
        val serviceIntent = Intent(context, YouTubeBackgroundService::class.java)
        
        try {
            serviceRule.startService(serviceIntent)
            // 如果没有抛出异常，测试通过
            assert(true)
        } catch (e: Exception) {
            // 服务应该能优雅处理null extras
            assert(false) { "Service should handle null extras gracefully" }
        }
    }

    @Test
    fun testServiceForegroundNotification() {
        val serviceIntent = Intent(context, YouTubeBackgroundService::class.java).apply {
            putExtra("url", "https://m.youtube.com/watch?v=test")
        }

        // 启动前台服务
        context.startForegroundService(serviceIntent)
        
        // 等待服务启动
        Thread.sleep(2000)
        
        // 验证通知存在（简化检查）
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) 
            as android.app.NotificationManager
        
        // 检查通知渠道是否创建
        val channels = notificationManager.notificationChannels
        val hasYouTubeChannel = channels.any { it.id == "youtube_background_channel" }
        
        assert(hasYouTubeChannel)
        
        // 停止服务
        context.stopService(serviceIntent)
    }

    @Test
    fun testServiceBindingReturnsNull() {
        val serviceIntent = Intent(context, YouTubeBackgroundService::class.java)
        
        // 尝试绑定服务
        val binder = serviceRule.bindService(serviceIntent)
        
        // 验证绑定返回null（因为这是一个启动服务，不是绑定服务）
        assert(binder == null)
    }
}
