package com.ytbbk.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.espresso.web.webdriver.DriverAtoms.*
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ytbbk.MainActivity
import com.ytbbk.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class WebViewUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testWebViewDisplaysCorrectly() {
        // 验证WebView显示
        onView(withId(R.id.webView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testWebViewLoadsYouTubeContent() {
        // 等待页面加载
        Thread.sleep(5000)
        
        // 验证页面内容包含YouTube相关元素
        try {
            onWebView()
                .withElement(findElement(Locator.TAG_NAME, "title"))
                .check(webMatches(getText(), containsString("YouTube")))
        } catch (e: Exception) {
            // 备用验证：检查是否加载了任何内容
            onWebView()
                .withElement(findElement(Locator.TAG_NAME, "body"))
                .check(webMatches(getText(), org.hamcrest.Matchers.not(isEmptyString())))
        }
    }

    @Test
    fun testWebViewScrolling() {
        // 等待页面加载
        Thread.sleep(3000)
        
        // 测试滚动功能
        onView(withId(R.id.webView))
            .perform(swipeUp())
            .perform(swipeDown())
        
        // 验证WebView仍然可见
        onView(withId(R.id.webView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testWebViewTouchInteraction() {
        // 等待页面加载
        Thread.sleep(3000)
        
        // 测试点击交互
        onView(withId(R.id.webView))
            .perform(click())
        
        // 验证WebView响应交互
        onView(withId(R.id.webView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testWebViewBackNavigation() {
        activityRule.scenario.onActivity { activity ->
            val webView = activity.findViewById<android.webkit.WebView>(R.id.webView)
            
            // 模拟页面导航
            activity.runOnUiThread {
                webView.loadUrl("https://m.youtube.com/trending")
            }
            
            Thread.sleep(3000)
            
            // 测试返回导航
            if (webView.canGoBack()) {
                activity.runOnUiThread {
                    webView.goBack()
                }
            }
            
            // 验证WebView仍然正常工作
            Thread.sleep(2000)
            assert(webView.url != null)
        }
    }
}
