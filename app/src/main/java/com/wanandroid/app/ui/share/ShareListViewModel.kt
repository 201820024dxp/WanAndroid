package com.wanandroid.app.ui.share

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.wanandroid.app.logic.model.Article
import com.wanandroid.app.logic.repository.ProfileRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow

class ShareListViewModel : ViewModel() {

    private val _fetchShare = Channel<String>(Channel.CONFLATED)

    /**
     * 接收用户信息
     */
    val shareResponseFlow = ProfileRepository.shareResponseFlow

    /**
     * 分享列表数据流
     */
    val shareListFlow = _fetchShare.receiveAsFlow().flatMapLatest {
        ProfileRepository.getUserShareList(it)
    }.cachedIn(viewModelScope)

    fun fetch(userId: String) {
        _fetchShare.trySend(userId).isSuccess
    }

}