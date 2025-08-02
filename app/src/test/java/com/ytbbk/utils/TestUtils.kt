package com.ytbbk.utils

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import org.mockito.Mockito

object TestUtils {
    
    fun getTestContext(): Context {
        return ApplicationProvider.getApplicationContext()
    }
    
    fun createTestIntent(url: String = "https://m.youtube.com/watch?v=test"): Intent {
        return Intent().apply {
            putExtra("url", url)
        }
    }
    
    fun waitForCondition(
        timeoutMs: Long = 5000,
        intervalMs: Long = 100,
        condition: () -> Boolean
    ): Boolean {
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() - startTime < timeoutMs) {
            if (condition()) {
                return true
            }
            Thread.sleep(intervalMs)
        }
        return false
    }
    
    inline fun <reified T> mockService(): T {
        return Mockito.mock(T::class.java)
    }
    
    fun simulateNetworkDelay(delayMs: Long = 1000) {
        Thread.sleep(delayMs)
    }
}
