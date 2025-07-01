package com.wanandroid.app.ui.web

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ActivityWebBinding
import com.wanandroid.app.http.ServiceCreator

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val url = intent.getStringExtra("url")
        binding.webView.settings.javaScriptEnabled=true
        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl(url ?: ServiceCreator.BASE_URL)
    }
}