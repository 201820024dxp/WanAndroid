package com.wanandroid.app.logic.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wanandroid.app.base.IntKeyPagingSource
import com.wanandroid.app.logic.network.impl.NavigationServiceNetwork

object NavigationRepository {

    suspend fun getNavigationList() =
        NavigationServiceNetwork.getNavigationList().data ?: emptyList()

    suspend fun getSystemChapterList() =
        NavigationServiceNetwork.getSystemChapterList().data ?: emptyList()

    fun getSystemArticleList(cid: Int, pageSize: Int) =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            )
        ) {
            IntKeyPagingSource(
                pageStart = 0,  // 系统文章列表从第0页开始
                block = { page, size ->
                    NavigationServiceNetwork.getSystemArticleList(page, cid, size).data?.datas
                        ?: emptyList()
                }
            )
        }.flow

}