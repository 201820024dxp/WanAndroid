package com.wanandroid.app.logic.network.impl

import com.wanandroid.app.http.ServiceCreator
import com.wanandroid.app.http.catch
import com.wanandroid.app.logic.network.CoinService
import retrofit2.await

object CoinServiceNetwork {

    private val coinService: CoinService by lazy { ServiceCreator.create() }

    suspend fun getSelfCoinInfo() = catch { coinService.getSelfCoinInfo().await() }

    suspend fun getCoinHistoryList(pageNo: Int) =
        catch { coinService.getCoinHistoryList(pageNo).await() }

    suspend fun getCoinRankList(pageNo: Int) =
        catch { coinService.getCoinRankList(pageNo).await() }
}