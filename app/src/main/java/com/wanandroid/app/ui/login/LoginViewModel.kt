package com.wanandroid.app.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.wanandroid.app.logic.model.Login
import com.wanandroid.app.logic.repository.AccountRepository

class LoginViewModel : ViewModel() {
    // 获取 用户名 和 密码 输入值
    var loginUserName = MutableLiveData<String>()
    var loginPassword = MutableLiveData<String>()

    // 监听登录请求返回值
    private val _loginLiveData = MutableLiveData<Login>()
    val loginLiveData = _loginLiveData.switchMap {
        AccountRepository.login(it.username, it.password)
    }

    /**
     * 发送登录请求
     */
    fun login(username: String, password: String) {
        _loginLiveData.value = Login(username, password)
    }
}