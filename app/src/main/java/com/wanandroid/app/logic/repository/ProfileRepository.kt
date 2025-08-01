package com.wanandroid.app.logic.repository

import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wanandroid.app.base.IntKeyPagingSource
import com.wanandroid.app.logic.model.ShareResponse
import com.wanandroid.app.logic.network.impl.ProfileServiceNetwork
import kotlinx.coroutines.flow.MutableSharedFlow

object ProfileRepository {

    private val _shareResponseFlow = MutableSharedFlow<ShareResponse>(1)
    val shareResponseFlow = _shareResponseFlow

    fun getUserShareList(userId: String) =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        ) {
            IntKeyPagingSource(
                pageStart = 1,  // 用户分享列表从第1页开始
                block = { page, pageSize ->
                    val shareResponse = ProfileServiceNetwork.getUserShareList(userId, page).data
                    // 获取用户信息
                    shareResponse?.let {
                        _shareResponseFlow.tryEmit(shareResponse)
                    }
                    // 将获取到的分享列表返回
                    shareResponse?.shareArticles?.datas ?: emptyList()
                }
            )
        }.flow

    fun postShareArticle(title: String, link: String) = liveData {
        emit(ProfileServiceNetwork.postShareArticle(title, link))
    }

    fun getToolList() = liveData {
        emit(ProfileServiceNetwork.getToolList().data ?: emptyList())
    }
}