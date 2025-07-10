package com.wanandroid.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.wanandroid.app.logic.repository.SearchRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow

class SearchViewModel : ViewModel() {

    private val _fetchSearch = Channel<String>(Channel.CONFLATED)

    /**
     * 获取搜索结果文章列表
     */
    val searchResponseFlow = _fetchSearch.receiveAsFlow().flatMapLatest {
        SearchRepository.getSearchResults(20, it)
    }.cachedIn(viewModelScope)

    fun search(query: String) {
        _fetchSearch.trySend(query).isSuccess
    }
}