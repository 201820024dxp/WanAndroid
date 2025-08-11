package com.wanandroid.app.app

import android.app.Application
import android.content.Context
import android.util.Log
import android.webkit.WebView
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.wanandroid.app.ui.account.AccountManager

class WanAndroidApp: Application(), ViewModelStoreOwner {

    private val _viewModelStore by lazy { ViewModelStore() }

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        // 获得全局上下文
        appContext = applicationContext
        // init AccountManager
        AccountManager.init(appContext)
        // init App References
        AppPreferences.init(appContext)
        // 开启WebView Debug模式
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)

//        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
//            Log.e("GlobalCrash", "Uncaught exception", throwable)
//        }
    }

    /**
     * 实现全局收藏需要使用 App 级别的 ViewModel
     * 这要求 baseApp 持有 ViewModelStoreOwner
     * ViewModelStoreOwner 必须重写 viewModelStore 属性
     */
    override val viewModelStore: ViewModelStore
        get() = _viewModelStore
}