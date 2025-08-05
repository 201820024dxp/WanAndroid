package com.wanandroid.app.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.wanandroid.app.logic.repository.AccountRepository

class SettingViewModel : ViewModel() {

    private val _logoutLiveData = MutableLiveData<Any>()
    val logoutLiveData = _logoutLiveData.switchMap {
        AccountRepository.logout()
    }

    fun logout() {
        _logoutLiveData.value = _logoutLiveData.value
    }
}