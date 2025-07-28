package com.wanandroid.app.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.wanandroid.app.logic.model.UserInfo
import com.wanandroid.app.logic.repository.AccountRepository

class ProfileViewModel : ViewModel() {

    private val _userInfo = MutableLiveData<Any>()
    val userInfo = _userInfo.switchMap {
        val userInfo1 = AccountRepository.getUserInfo()
        Log.d(this.javaClass.simpleName, userInfo1.toString())
        userInfo1
    }

    fun getUserInfo() {
        _userInfo.value = _userInfo.value
    }

}