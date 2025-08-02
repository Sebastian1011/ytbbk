package com.ytbbk

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.*
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ytbbk.databinding.ActivityMainBinding
import com.ytbbk.service.YouTubeBackgroundService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private var currentUrl: String = "https://m.youtube.com"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupWebView()
        loadYouTube()
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                allowContentAccess = true
                allowFileAccess = true
                mediaPlaybackRequiresUserGesture = false
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                cacheMode = WebSettings.LOAD_DEFAULT
                userAgentString = "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36"
            }
            
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    currentUrl = url ?: ""
                    injectBackgroundPlayScript()
                }
                
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    return false
                }
            }
            
            webChromeClient = object : WebChromeClient() {
                override fun onPermissionRequest(request: PermissionRequest?) {
                    request?.grant(request.resources)
                }
            }
        }
    }
    
    private fun loadYouTube() {
        binding.webView.loadUrl("https://m.youtube.com")
    }
    
    private fun injectBackgroundPlayScript() {
        val script = """
            (function() {
                var videos = document.querySelectorAll('video');
                videos.forEach(function(video) {
                    video.addEventListener('pause', function() {
                        if (document.hidden) {
                            setTimeout(function() {
                                video.play();
                            }, 100);
                        }
                    });
                });
                
                document.addEventListener('visibilitychange', function() {
                    if (document.hidden) {
                        var videos = document.querySelectorAll('video');
                        videos.forEach(function(video) {
                            if (video.paused) {
                                video.play();
                            }
                        });
                    }
                });
            })();
        """.trimIndent()
        
        binding.webView.evaluateJavascript(script, null)
    }
    
    override fun onPause() {
        super.onPause()
        startBackgroundService()
        
        // 确保视频继续播放
        lifecycleScope.launch {
            delay(500)
            binding.webView.evaluateJavascript("""
                var videos = document.querySelectorAll('video');
                videos.forEach(function(video) {
                    if (video.paused) {
                        video.play();
                    }
                });
            """.trimIndent(), null)
        }
    }
    
    override fun onResume() {
        super.onResume()
        stopBackgroundService()
    }
    
    private fun startBackgroundService() {
        val intent = Intent(this, YouTubeBackgroundService::class.java)
        intent.putExtra("url", currentUrl)
        startForegroundService(intent)
    }
    
    private fun stopBackgroundService() {
        val intent = Intent(this, YouTubeBackgroundService::class.java)
        stopService(intent)
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
            binding.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    
    override fun onDestroy() {
        binding.webView.destroy()
        super.onDestroy()
    }
}
