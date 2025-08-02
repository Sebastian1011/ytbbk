package com.ytbbk.service

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.ytbbk.MainActivity
import com.ytbbk.R

class YouTubeBackgroundService : Service() {
    
    private var wakeLock: PowerManager.WakeLock? = null
    private val channelId = "youtube_background_channel"
    private val notificationId = 1001
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        acquireWakeLock()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra("url") ?: "YouTube"
        startForeground(notificationId, createNotification(url))
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelId,
            "YouTube后台播放",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "YouTube视频后台播放服务"
            setShowBadge(false)
        }
        
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
    
    private fun createNotification(url: String): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("YouTube后台播放")
            .setContentText("正在后台播放YouTube视频")
            .setSmallIcon(R.drawable.ic_play_arrow)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }
    
    private fun acquireWakeLock() {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "YTbbk::BackgroundPlayWakeLock"
        )
        wakeLock?.acquire(10 * 60 * 1000L) // 10 minutes
    }
    
    override fun onDestroy() {
        wakeLock?.release()
        super.onDestroy()
    }
}
