package com.wanandroid.app.logic.network.impl

import com.wanandroid.app.http.ServiceCreator
import com.wanandroid.app.http.catch
import com.wanandroid.app.logic.network.HomeService
import retrofit2.await

object HomeServiceNetwork {

    private val homeService by lazy { ServiceCreator.create<HomeService>() }

    suspend fun getBanner() = catch { homeService.getBanner().await() }

    suspend fun getArticleTopList() = catch { homeService.getArticleTopList().await() }

    suspend fun getArticlePageList(pageNo: Int, pageSize: Int) =
        catch { homeService.getArticlePageList(pageNo, pageSize).await() }

    suspend fun getSquareArticlePageList(pageNo: Int, pageSize: Int) =
        catch { homeService.getSquareArticlePageList(pageNo, pageSize).await() }

    suspend fun getAnswerPageList(pageNo: Int, pageSize: Int) =
        catch { homeService.getAnswerPageList(pageNo, pageSize).await() }
}