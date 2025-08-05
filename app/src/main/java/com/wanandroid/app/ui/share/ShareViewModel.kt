package com.wanandroid.app.ui.share

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.wanandroid.app.logic.model.ArticleShareBean
import com.wanandroid.app.logic.repository.ProfileRepository

class ShareViewModel : ViewModel() {
    // 获取 文章标题 和 文章链接 输入值
    var shareTitle = MutableLiveData<String>()
    var shareLink = MutableLiveData<String>()

    // 监听分享结果
    private val _shareLiveData = MutableLiveData<ArticleShareBean>()
    val shareLiveData = _shareLiveData.switchMap {
        ProfileRepository.postShareArticle(it.title, it.link)
    }

    /**
     * 分享文章
     */
    fun share(title: String, link: String) {
        _shareLiveData.value = ArticleShareBean(title, link)
    }
}