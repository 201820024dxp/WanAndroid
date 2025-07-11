package com.wanandroid.app.ui.navigation.child.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.wanandroid.app.logic.repository.NavigationRepository

class NavigationChildViewModel : ViewModel() {

    val navigationList = liveData {
        emit(NavigationRepository.getNavigationList())
    }

}