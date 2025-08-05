package com.wanandroid.app.ui.web

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ActivityWebBinding
import com.wanandroid.app.eventbus.FlowBus
import com.wanandroid.app.logic.model.Web
import com.wanandroid.app.logic.repository.CollectRepository
import com.wanandroid.app.ui.account.AccountManager
import com.wanandroid.app.utils.showShortToast

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
    private val TAG = this.javaClass.simpleName

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
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    // 页面开始加载时注入 JavaScript
                    view?.evaluateJavascript("""
                        (function() {
                            function isInJuejinApp() { return true; }
                            // 使用 setInterval 定时检查按钮元素是否存在
                            var interval = setInterval(function() {                            
                                // 稀土掘金的“继续”按钮元素
                                var cancelButton = document.querySelector('button.btn.cancel-btn');
                                console.log(openAppLink);
                                // CSDN的“继续”按钮元素
                                var openAppLink = document.querySelector('a.open-app.open-app-weixin');
                                console.log(openAppLink);
                                
                                if (cancelButton) {
                                    console.log('Cancel button found, clicking it...');
                                    cancelButton.click(); // 自动点击按钮
                                    clearInterval(interval); // 点击后停止定时器
                                }
                                // 如果找到了打开应用的链接，执行点击操作
                                if (openAppLink) {
                                    console.log('Open App link found, clicking it...');
                                    openAppLink.click(); // 自动点击链接
                                    clearInterval(interval); // 点击后停止定时器
                                }
                            }, 100); // 每100毫秒检查一次
                        })();
                    """) { result ->
                        // 可以获取执行结果（如果有）
                    }
                }
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    // 获取当前Url，供分享使用
                    currentUrl = url ?: ""
                    Log.d(TAG, "onPageFinished")

                    // 页面加载完成后注入 JavaScript 隐藏元素
//                    view?.evaluateJavascript("""
//                        (function() {
//                            var clazzList = ['.drawer', '.open-button']
//                            for (var i = 0; i < clazzList.length; i++){
//                                var elements = document.querySelectorAll(clazzList[i]);
//                                for (var j = 0; j < elements.length; j++) {
//                                    elements[j].style.display = 'none'; // 隐藏该元素
//                                }
//                            }
//                        })();
//                    """) { result ->
//                        // 可以获取执行结果（如果需要的话）
//                    }
                }
            })
            .setWebChromeClient(object : WebChromeClient() {
                override fun onReceivedTitle(p0: WebView?, p1: String?) {
                    super.onReceivedTitle(p0, p1)
                    binding.title.text = p1 ?: ""
                }

                override fun onJsAlert(
                    p0: WebView?,
                    p1: String?,
                    p2: String?,
                    p3: JsResult?
                ): Boolean {
                    // 拦截弹窗，避免显示默认弹窗
                    Log.d(TAG, "onJsAlert run, p1:$p1, p2:$p2, p3:$p3")
                    p3?.cancel()
                    return true // 返回 true，表示拦截
                }

                override fun onJsConfirm(
                    p0: WebView?,
                    p1: String?,
                    p2: String?,
                    p3: JsResult?
                ): Boolean {
                    // 拦截确认弹窗
                    Log.d(TAG, "onJsConfirm run, p1:$p1, p2:$p2, p3:$p3")
                    p3?.cancel()
                    return true
                }

                override fun onJsPrompt(
                    p0: WebView?,
                    p1: String?,
                    p2: String?,
                    p3: String?,
                    p4: JsPromptResult?
                ): Boolean {
                    // 拦截提示弹窗
                    Log.d(TAG, "onJsConfirm run, p1:$p1, p2:$p2, p3:$p3")
                    p4?.cancel()
                    return true
                }
            })
            .setMainFrameErrorView(R.layout.container_error_layout, -1)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
            .interceptUnkownUrl() //拦截找不到相关页面的Scheme
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
        binding.collect.visibility =
            if (intentData.isNeedShowCollectIcon()) View.VISIBLE else View.GONE
        // 返回按钮
        binding.toolbar.setNavigationOnClickListener { finish() }

        // init event
        binding.collect.setOnClickListener {
            // 收藏事件 ? 收藏图标 : 未收藏图标
            AccountManager.checkLogin(this) {
                CollectRepository.changeArticleCollectStateById(intentData.id, intentData.collect)
                    .observeForever {
                        when (it.errorCode) {
                            0 -> {
                                intentData.collect = !intentData.collect
                                binding.collect.setImageResource(   // 更新收藏状态
                                    if (intentData.collect) R.drawable.ic_collect
                                    else R.drawable.ic_un_collect
                                )
                                // 发送收藏状态的改变
                                FlowBus.collectStateFlow.tryEmit(
                                    FlowBus.CollectStateChangedItem(
                                        intentData.id, intentData.collect
                                    )
                                )
                            }

                            else -> {
                                it.errorMsg.showShortToast()
                            }
                        }
                    }
            }
        }
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
                            .putExtra(
                                Intent.EXTRA_TEXT,
                                "${binding.title.text}: $currentUrl"
                            )
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