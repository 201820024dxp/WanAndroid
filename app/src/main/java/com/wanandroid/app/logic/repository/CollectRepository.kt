package com.wanandroid.app.logic.repository

import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wanandroid.app.base.IntKeyPagingSource
import com.wanandroid.app.logic.network.impl.CollectServiceNetwork

object CollectRepository {

    fun getCollectList(pageSize: Int = 20) = Pager(
        config = PagingConfig(
            pageSize = pageSize
        )
    ) {
        IntKeyPagingSource(
            pageStart = 0   // request index starting from page 0
        ) { page, size ->
            CollectServiceNetwork.getCollectList(page, size).data?.datas ?: emptyList()
        }
    }.flow

    fun changeArticleCollectStateById(id: Int, collect: Boolean) = liveData {
        if (collect) {  // 如果初始为收藏状态 则取消收藏
            emit(CollectServiceNetwork.unCollectArticleById(id))
        } else {        // 与上述相反
            emit(CollectServiceNetwork.collectArticleById(id))
        }
    }
}