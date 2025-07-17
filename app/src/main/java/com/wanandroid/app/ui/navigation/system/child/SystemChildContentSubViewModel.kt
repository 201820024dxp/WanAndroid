package com.wanandroid.app.ui.navigation.system.child

import androidx.lifecycle.ViewModel
import com.wanandroid.app.logic.repository.NavigationRepository

class SystemChildContentSubViewModel : ViewModel() {

    fun getSystemArticleList(cid: Int, pageSize: Int = 20) =
        NavigationRepository.getSystemArticleList(cid, pageSize)

}