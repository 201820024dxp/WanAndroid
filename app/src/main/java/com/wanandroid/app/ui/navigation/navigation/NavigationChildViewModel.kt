package com.wanandroid.app.ui.navigation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.wanandroid.app.logic.repository.NavigationRepository

class NavigationChildViewModel : ViewModel() {

    val navigationListLiveData = liveData {
        emit(NavigationRepository.getNavigationList())
    }

}