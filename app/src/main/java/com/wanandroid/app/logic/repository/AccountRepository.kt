package com.wanandroid.app.logic.repository

import androidx.lifecycle.liveData
import com.wanandroid.app.logic.network.impl.AccountServiceNetwork

object AccountRepository {
    /**
     * 登录
     */
    fun login(username: String, password: String) =
        liveData{ emit(AccountServiceNetwork.login(username, password)) }

    /**
     * 注册
     */
    fun register(username: String, password: String, rePassword: String) =
        liveData { emit(AccountServiceNetwork.register(username, password, rePassword)) }

    /**
     * 退出
     */
    fun logout() = liveData { emit(AccountServiceNetwork.logout()) }

    /**
     * 获取用户信息
     */
    fun getUserInfo() = liveData { emit(AccountServiceNetwork.getUserInfo().data) }
}