package com.wanandroid.app.logic.repository

import android.util.Log
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
}