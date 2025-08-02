package com.ytbbk

import android.content.Intent
import android.webkit.WebView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowWebView

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class MainActivityTest {

    @Before
    fun setUp() {
        ShadowWebView.setWebContentsDebuggingEnabled(true)
    }

    @Test
    fun `activity should launch successfully`() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                assert(activity != null)
                assert(!activity.isFinishing)
            }
        }
    }

    @Test
    fun `webview should be initialized with correct settings`() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val webView = activity.findViewById<WebView>(R.id.webView)
                assert(webView != null)
                assert(webView.settings.javaScriptEnabled)
                assert(webView.settings.domStorageEnabled)
            }
        }
    }

    @Test
    fun `activity should handle pause and resume correctly`() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                // 模拟暂停
                scenario.moveToState(androidx.lifecycle.Lifecycle.State.STARTED)
                
                // 模拟恢复
                scenario.moveToState(androidx.lifecycle.Lifecycle.State.RESUMED)
                
                assert(!activity.isFinishing)
            }
        }
    }

    @Test
    fun `activity should handle back button correctly`() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val webView = activity.findViewById<WebView>(R.id.webView)
                
                // 模拟WebView有历史记录
                val shadowWebView = org.robolectric.Shadows.shadowOf(webView)
                shadowWebView.pushEntryToHistory("https://m.youtube.com")
                shadowWebView.pushEntryToHistory("https://m.youtube.com/watch?v=test")
                
                // 测试返回键处理
                val keyEvent = android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_BACK)
                val result = activity.onKeyDown(android.view.KeyEvent.KEYCODE_BACK, keyEvent)
                assert(result == true)
            }
        }
    }
}
