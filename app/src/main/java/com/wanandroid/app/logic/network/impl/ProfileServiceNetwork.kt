package com.wanandroid.app.logic.network.impl

import com.wanandroid.app.http.ServiceCreator
import com.wanandroid.app.http.catch
import com.wanandroid.app.logic.network.ProfileService
import retrofit2.await

object ProfileServiceNetwork {

    private val profileService by lazy { ServiceCreator.create<ProfileService>() }

    /**
     * 获取用户分享列表
     */
    suspend fun getUserShareList(userId: String, page: Int) =
        catch { profileService.getUserShareList(userId, page).await() }

    /**
     * 分享文章
     */
    suspend fun postShareArticle(title: String, link: String) =
        catch { profileService.postShareArticle(title, link).await() }

    /**
     * 删除分享文章
     */
    suspend fun deleteShareArticle(id: Int) =
        catch { profileService.deleteShareArticle(id).await() }

    /**
     * 工具列表
     */
    suspend fun getToolList() = catch { profileService.getToolList().await() }
}