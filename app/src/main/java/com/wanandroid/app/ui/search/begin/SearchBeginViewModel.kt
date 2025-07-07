package com.wanandroid.app.ui.search.begin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.wanandroid.app.logic.model.HotKey
import com.wanandroid.app.logic.repository.SearchRepository

class SearchBeginViewModel : ViewModel() {

    var hotKeyList = mutableListOf<HotKey>()

    val searchHotKeyLiveData = liveData<List<HotKey>> {
        emit(
            SearchRepository.getSearchHotKey().data ?: emptyList()
        )
    }

}