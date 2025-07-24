package com.wanandroid.app.ui.account

import com.wanandroid.app.logic.model.User

sealed interface AccountState {
    data object LogOut : AccountState

    data class LogIn(val isFromCookie: Boolean, val user: User? = null) : AccountState
}