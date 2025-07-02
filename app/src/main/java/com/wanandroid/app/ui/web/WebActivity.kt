package com.wanandroid.app.ui.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ActivityWebBinding
import com.wanandroid.app.http.ServiceCreator
import com.wanandroid.app.logic.model.Web

class WebActivity : AppCompatActivity() {

    companion object {
        const val KEY_WEB_VIEW_BUNDLE = "key_web_view_bundle"

        fun loadUrl(context: Context, data: Web.WebIntent) {
            context.startActivity(
                Intent(context, WebActivity::class.java).apply {
                    putExtra(KEY_WEB_VIEW_BUNDLE, data)
                }
            )
        }

        fun loadUrl(context: Context, url: String) {
            context.startActivity(
                Intent(context, WebActivity::class.java).apply {
                    putExtra(KEY_WEB_VIEW_BUNDLE, Web.WebIntent(url))
                }
            )
        }
    }

    private lateinit var binding: ActivityWebBinding

    // Using AgentWeb for better web view management
    private val agentWeb by lazy {
        AgentWeb.with(this)
            .setAgentWebParent(
                binding.webContainer,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            .useDefaultIndicator(
                resources.getColor(R.color.md_theme_primary, theme)
            )
            .setWebViewClient(object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    currentUrl = url ?: ""
                }
            })
            .setWebChromeClient(object : WebChromeClient() {
                override fun onReceivedTitle(p0: WebView?, p1: String?) {
                    super.onReceivedTitle(p0, p1)
                    binding.title.text = p1 ?: ""
                }
            })
            .createAgentWeb()
            .ready()
            .get()
    }

    // 当前URL，用于分享时使用
    private var currentUrl: String = ""

    // 获取传入的Intent数据
    private val intentData by lazy {
        IntentCompat.getParcelableExtra(intent, KEY_WEB_VIEW_BUNDLE, Web.WebIntent::class.java)
            ?: Web.WebIntent("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // init view
        agentWeb.urlLoader.loadUrl(intentData.url)
        setSupportActionBar(binding.toolbar)
        binding.collect.setImageResource(   // 是否收藏当前网页
            if (intentData.collect) R.drawable.ic_collect else R.drawable.ic_un_collect
        )
        // 传入id则显示收藏图标，否则隐藏
        binding.collect.visibility = if (intentData.isNeedShowCollectIcon()) View.VISIBLE else View.GONE
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.collect.setOnClickListener { /* TODO：收藏事件 ? 收藏图标 : 未收藏图标 */ }
    }

    override fun onResume() {
        super.onResume()
        agentWeb.webLifeCycle.onResume()
    }

    override fun onPause() {
        super.onPause()
        agentWeb.webLifeCycle.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        agentWeb.webLifeCycle.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: android.view.KeyEvent?): Boolean {
        if (agentWeb.handleKeyEvent(keyCode, event)) return true
        return super.onKeyDown(keyCode, event)
    }

    /**
     * 绑定Web展示页面的菜单栏
     */
    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.web_action_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * 处理菜单栏的点击事件
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.web_action_refresh -> {
                agentWeb.urlLoader.reload()
                true
            }
            R.id.web_action_share -> {
                startActivity(
                    Intent.createChooser(
                        Intent(Intent.ACTION_SEND)
                            .putExtra( Intent.EXTRA_TEXT,
                                "${binding.title.text}: $currentUrl")
                            .setType("text/plain"), "分享至"
                    )
                )
                true
            }
            R.id.web_action_open_in_browser -> {
                startActivity(
                    Intent(Intent.ACTION_VIEW).setData(currentUrl.toUri())
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}