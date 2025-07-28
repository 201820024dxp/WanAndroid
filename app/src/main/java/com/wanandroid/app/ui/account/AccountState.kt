package com.wanandroid.app.ui.account

import android.content.Context
import android.content.Intent
import com.wanandroid.app.logic.model.User
import com.wanandroid.app.ui.login.LoginActivity
import com.wanandroid.app.utils.showShortToast

sealed interface AccountState {
    data object LogOut : AccountState

    data class LogIn(val isFromCookie: Boolean, val user: User? = null) : AccountState
}

inline val AccountState.isLogin: Boolean
    get() {
        return this is AccountState.LogIn
    }

inline fun AccountState.checkLogin(context: Context, action: (AccountState) -> Unit) {
    if (this.isLogin) {
        action(this)
    } else {
        "请先登录".showShortToast()
        context.startActivity(Intent(context, LoginActivity::class.java))
    }
}