package com.wanandroid.app.logic.network

import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.CoinHistory
import com.wanandroid.app.logic.model.CoinInfo
import com.wanandroid.app.logic.model.PageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinService {

    /**
     * 获取个人积分信息
     */
    @GET("lg/coin/userinfo/json")
    fun getSelfCoinInfo() : Call<NetworkResponse<CoinInfo>>

    /**
     * 获取个人积分记录
     * @param pageNo 页码
     */
    @GET("lg/coin/list/{pageNo}/json")
    fun getCoinHistoryList(
        @Path("pageNo") pageNo: Int
    ) : Call<NetworkResponse<PageResponse<CoinHistory>>>

    /**
     * 获取积分排行榜
     * @param pageNo 页码
     */
    @GET("coin/rank/{pageNo}/json")
    fun getCoinRankList(
        @Path("pageNo") pageNo: Int
    ) : Call<NetworkResponse<PageResponse<CoinInfo>>>
}