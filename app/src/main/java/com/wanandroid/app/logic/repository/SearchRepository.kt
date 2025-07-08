package com.wanandroid.app.logic.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wanandroid.app.base.IntKeyPagingSource
import com.wanandroid.app.logic.network.impl.SearchNetwork

object SearchRepository {

    suspend fun getSearchHotKey() = SearchNetwork.getSearchHotKey()

    fun getSearchResults(pageSize: Int, key: String) =
        Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
        ) {
            IntKeyPagingSource(
                pageStart = 0,      // 搜索结果从第0页开始
                block = { page, pageSize ->
                    SearchNetwork.getSearchResults(page, pageSize, key).data?.datas ?: emptyList()
                }
            )
        }.flow

}