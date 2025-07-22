package com.wanandroid.app.logic.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wanandroid.app.base.IntKeyPagingSource
import com.wanandroid.app.logic.network.impl.GroupServiceNetwork

object GroupRepository {

    /**
     * 公众号列表
     */
    suspend fun getGroupChapterList() =
        GroupServiceNetwork.getGroupChapterList().data ?: emptyList()

    /**
     * 公众号文章列表
     */
    fun getGroupArticleList(id: Int, pageSize: Int = 20) =
        Pager(
            config = PagingConfig(pageSize)
        ) {
            IntKeyPagingSource(
                pageStart = 1,  // 公众号文章列表从第一页开始
            ) { page, size ->
                GroupServiceNetwork.getGroupArticleListById(id, page, size).data?.datas
                    ?: emptyList()
            }
        }.flow
}