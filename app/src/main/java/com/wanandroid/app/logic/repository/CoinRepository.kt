package com.wanandroid.app.logic.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wanandroid.app.base.IntKeyPagingSource
import com.wanandroid.app.logic.model.CoinInfo
import com.wanandroid.app.logic.network.impl.CoinServiceNetwork

object CoinRepository {
    /**
     * 获取个人积分信息
     */
    suspend fun getSelfCoinInfo() = CoinServiceNetwork.getSelfCoinInfo().data ?: CoinInfo()

    /**
     * 获取个人积分记录
     */
    fun getCoinHistoryList() =
        Pager(
            config = PagingConfig(pageSize = 20)    // 个人积分列表不支持调整分页大小，固定为 20
        ) {
            IntKeyPagingSource(
                pageStart = 1,  // 个人积分列表网址id从 1 开始
            ) { page, _ ->
                CoinServiceNetwork.getCoinHistoryList(page).data?.datas ?: emptyList()
            }
        }.flow

    /**
     * 获取积分排行榜
     */
    fun getCoinRankList() =
        Pager(
            config = PagingConfig(pageSize = 30)    // 积分排行榜不支持调整分页大小，固定为 30
        ) {
            IntKeyPagingSource(
                pageStart = 1,  // 积分排行榜网址id从 1 开始
            ) { page, _ ->
                CoinServiceNetwork.getCoinRankList(page).data?.datas ?: emptyList()
            }
        }.flow
}