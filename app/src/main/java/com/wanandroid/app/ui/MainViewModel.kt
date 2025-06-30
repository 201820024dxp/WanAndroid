package com.wanandroid.app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    /**
     * 保存首页导航栏点击的tab
     */
    private val _mainTabClickLiveData = MutableLiveData<String>()
    val mainTabClickLiveData : LiveData<String> = _mainTabClickLiveData

    fun bottomTabClick(tag: String) {
        _mainTabClickLiveData.value = tag
    }

}