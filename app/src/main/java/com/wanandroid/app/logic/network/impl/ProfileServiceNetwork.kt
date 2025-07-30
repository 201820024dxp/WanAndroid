package com.wanandroid.app.logic.network.impl

import com.wanandroid.app.http.ServiceCreator
import com.wanandroid.app.logic.network.ProfileService
import retrofit2.await

object ProfileServiceNetwork {

    private val profileService by lazy { ServiceCreator.create<ProfileService>() }

    /**
     * 获取用户分享列表
     */
    suspend fun getUserShareList(userId: String, page: Int) =
        profileService.getUserShareList(userId, page).await()

    suspend fun postShareArticle(title: String, link: String) =
        profileService.postShareArticle(title, link).await()

}