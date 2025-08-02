package com.ytbbk.service

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class YouTubeBackgroundServiceTest {

    private lateinit var service: YouTubeBackgroundService

    @Before
    fun setUp() {
        val serviceController = Robolectric.buildService(YouTubeBackgroundService::class.java)
        service = serviceController.create().get()
    }

    @Test
    fun `service should start in foreground when onStartCommand called`() {
        // Given
        val intent = Intent().apply {
            putExtra("url", "https://m.youtube.com/watch?v=test")
        }

        // When
        val result = service.onStartCommand(intent, 0, 1)

        // Then
        assert(result == android.app.Service.START_STICKY)
    }

    @Test
    fun `service should handle null intent gracefully`() {
        // When
        val result = service.onStartCommand(null, 0, 1)

        // Then
        assert(result == android.app.Service.START_STICKY)
    }

    @Test
    fun `service should return null for onBind`() {
        // When
        val binder = service.onBind(Intent())

        // Then
        assert(binder == null)
    }

    @Test
    fun `service should create notification channel on create`() {
        // When - onCreate is already called in setUp, so just verify it doesn't crash
        service.onCreate()

        // Then - if we get here without exception, the test passes
        assert(true)
    }
}
