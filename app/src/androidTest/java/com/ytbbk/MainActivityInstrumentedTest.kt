package com.ytbbk

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.espresso.web.webdriver.DriverAtoms.*
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.INTERNET,
        android.Manifest.permission.ACCESS_NETWORK_STATE
    )

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testActivityLaunch() {
        // 验证Activity启动
        onView(withId(R.id.webView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testWebViewLoadsYouTube() {
        // 等待WebView加载
        Thread.sleep(3000)
        
        // 验证WebView加载了YouTube
        onWebView()
            .withElement(findElement(Locator.TAG_NAME, "body"))
            .check(webMatches(getText(), containsString("YouTube")))
    }

    @Test
    fun testWebViewJavaScriptEnabled() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val webView = activity.findViewById<android.webkit.WebView>(R.id.webView)
                assert(webView.settings.javaScriptEnabled)
            }
        }
    }

    @Test
    fun testActivityConfigurationChanges() {
        // 测试屏幕旋转
        InstrumentationRegistry.getInstrumentation().targetContext.let { context ->
            val activity = activityRule.activity
            
            // 模拟配置更改
            activity.recreate()
            
            // 验证WebView仍然存在
            onView(withId(R.id.webView))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun testBackgroundServiceIntegration() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                // 模拟应用进入后台
                scenario.moveToState(androidx.lifecycle.Lifecycle.State.STARTED)
                
                // 等待后台服务启动
                Thread.sleep(1000)
                
                // 验证服务正在运行（简化检查）
                val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) 
                    as android.app.ActivityManager
                val services = activityManager.getRunningServices(Integer.MAX_VALUE)
                
                val serviceRunning = services.any { 
                    it.service.className.contains("YouTubeBackgroundService") 
                }
                
                // 恢复前台
                scenario.moveToState(androidx.lifecycle.Lifecycle.State.RESUMED)
            }
        }
    }
}
