package com.wanandroid.app.ui.login.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.wanandroid.app.logic.model.Register
import com.wanandroid.app.logic.repository.AccountRepository

class RegisterDialogViewModel : ViewModel() {
    // 获取 用户名、密码和确认密码
    var registerUserName = MutableLiveData<String>()
    var registerPassword = MutableLiveData<String>()
    var registerConfirm = MutableLiveData<String>()

    // 监听注册请求返回值
    private val _registerLiveData = MutableLiveData<Register>()
    val registerLiveData = _registerLiveData.switchMap {
        AccountRepository.register(it.username, it.password, it.rePassword)
    }

    /**
     * 发送注册请求
     */
    fun register(username: String, password: String, confirm: String) {
        _registerLiveData.value = Register(username, password, confirm)
    }
}