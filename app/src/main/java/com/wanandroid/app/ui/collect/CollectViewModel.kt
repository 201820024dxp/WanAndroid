package com.wanandroid.app.ui.collect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.wanandroid.app.logic.repository.CollectRepository

class CollectViewModel : ViewModel() {

    val collectListFlow = CollectRepository.getCollectList(20).cachedIn(viewModelScope)

}