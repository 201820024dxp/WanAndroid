package com.wanandroid.app.ui.search.begin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.wanandroid.app.logic.model.HotKey
import com.wanandroid.app.logic.repository.SearchRepository
import com.wanandroid.app.utils.LimitedLRUQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchBeginViewModel : ViewModel() {

    // 热词列表
    var hotKeyList = mutableListOf<HotKey>()

    // 获取搜索热词
    val searchHotKeyLiveData = liveData<List<HotKey>> {
        emit( SearchRepository.getSearchHotKey()?.data ?: emptyList() )
    }

    // 搜索历史列表（上限20个）
    var searchHistoryList = LimitedLRUQueue<String>(20)

    // 获取搜索历史记录
    val searchHistoryFlow = SearchRepository.searchHistoryFlow
        .onEach { history ->
            searchHistoryList.clear()
            searchHistoryList.addAll(history.toList())
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptySet()
        )

    // 监听搜索历史变化
    private val _searchHistoryLiveData = MutableLiveData<List<String>>()
    val searchHistoryLiveData : LiveData<List<String>> = _searchHistoryLiveData

    // 添加搜索历史
    fun addSearchHistory(keyword: String) {
        if (searchHistoryList.add(keyword)) {
            _searchHistoryLiveData.value = searchHistoryList.toList()
            Log.d("SearchBeginViewModel", "now search history is: ${searchHistoryLiveData.value}")
        }
    }

    // 删除搜索历史
    fun removeSearchHistory(keyword: String) {
        if (searchHistoryList.remove(keyword)) {
            _searchHistoryLiveData.value = searchHistoryList.toList()
        }
    }

    override fun onCleared() {
        // 在ViewModel销毁时保存搜索历史到DataStore中
        // ViewModel销毁时会自动取消协程，因此不能使用viewModelScope
        val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            withContext(Dispatchers.Main) {
                SearchRepository.updateSearchHistory(searchHistoryList.toSet())
            }
        }
    }

}