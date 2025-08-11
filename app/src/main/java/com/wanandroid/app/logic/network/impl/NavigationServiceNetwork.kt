package com.wanandroid.app.logic.network.impl

import com.wanandroid.app.http.ServiceCreator
import com.wanandroid.app.http.catch
import com.wanandroid.app.logic.network.NavigationService
import retrofit2.await

object NavigationServiceNetwork {

    private val navigationService by lazy { ServiceCreator.create<NavigationService>() }

    suspend fun getNavigationList() =
        catch { navigationService.getNavigationList().await() }

    suspend fun getSystemChapterList() =
        catch { navigationService.getSystemChapterList().await() }

    suspend fun getSystemArticleList(pageNo: Int, cid: Int, pageSize: Int) =
        catch { navigationService.getSystemArticleList(pageNo, cid, pageSize).await() }

    suspend fun getCourseChapterList() =
        catch { navigationService.getCourseChapterList().await() }

    suspend fun getCourseListById(pageNo: Int, cid: Int, orderType: Int = 1, pageSize: Int = 20) =
        catch { navigationService.getCourseListById(pageNo, cid, orderType, pageSize).await() }

}