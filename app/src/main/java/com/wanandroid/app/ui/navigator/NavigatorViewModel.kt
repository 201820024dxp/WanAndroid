package com.wanandroid.app.ui.navigator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NavigatorViewModel : ViewModel() {

    val onRefresh = MutableLiveData<String>()

    val scrollToTop = MutableLiveData<String>()

}