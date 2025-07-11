package com.wanandroid.app.ui.navigation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NavigationViewModel : ViewModel() {

    val onRefresh = MutableLiveData<String>()

    val scrollToTop = MutableLiveData<String>()

}