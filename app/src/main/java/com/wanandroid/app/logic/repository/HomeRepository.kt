package com.wanandroid.app.logic.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wanandroid.app.base.IntKeyPagingSource
import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.Article
import com.wanandroid.app.logic.model.PageResponse
import com.wanandroid.app.logic.network.impl.HomeServiceNetwork
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

object HomeRepository {

    suspend fun getBanner() = HomeServiceNetwork.getBanner()

    suspend fun getArticleTopList() = HomeServiceNetwork.getArticleTopList()

    fun getArticlePageList(pageSize: Int) {
        Pager(
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            )
        ) {
            IntKeyPagingSource(
                pageStart = 0,
                block = {
                    getArticleTopList().data ?: emptyList()
                }
            )
        }.flow
    }

}