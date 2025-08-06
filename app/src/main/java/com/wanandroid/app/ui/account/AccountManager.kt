package com.wanandroid.app.ui.account

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.wanandroid.app.ui.login.LoginActivity
import com.wanandroid.app.utils.showShortToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.Cookie

object AccountManager {
    private const val PREFS_NAME = "WanAndroidCookies"
    private const val COOKIE_LOGIN_USER_NAME = "loginUserName_wanandroid_com"
    private const val COOKIE_TOKEN_PASS = "token_pass_wanandroid_com"
    private const val TAG = "AccountManager"

    private lateinit var sharedPreferences: SharedPreferences
    private val _isLogin = MutableStateFlow(false)  // 初始为未登录状态
    val isLogin: StateFlow<Boolean> get() = _isLogin

    // 初始化时检查SharedPreferences中是否有登录状态
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        var isUserNameValid = false
        var isTokenValid = false
        val cookieMaps = sharedPreferences.all
        for (map in cookieMaps) {
            try {
                val cookie = Gson().fromJson(map.value as String, Cookie::class.java)
                if ( cookie.name == COOKIE_LOGIN_USER_NAME
                    && cookie.expiresAt >= System.currentTimeMillis() ) {
                    isUserNameValid = true
                }
                if (cookie.name == COOKIE_TOKEN_PASS
                    && cookie.expiresAt >= System.currentTimeMillis()) {
                    isTokenValid = true
                }
            } catch (e: Exception) {
                // 记录错误并继续处理其他Cookie
                Log.e(TAG, "Error parsing cookie: ${e.message}")
                continue
            }
        }
        _isLogin.value = isUserNameValid && isTokenValid
    }

    // 设置登录状态
    fun setLoginStatus(isLoggedIn: Boolean) {
        _isLogin.value = isLoggedIn
    }

    fun checkLogin(context: Context, action: () -> Unit) {
        if (isLogin.value) {
            action()
        } else {
            "请先登录".showShortToast()
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

}