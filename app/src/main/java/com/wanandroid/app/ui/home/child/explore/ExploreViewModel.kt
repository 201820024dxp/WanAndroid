package com.wanandroid.app.ui.home.child.explore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.wanandroid.app.logic.repository.HomeRepository

class ExploreViewModel : ViewModel() {

    // 首页 Banner LiveData
    private val _bannerList = MutableLiveData<Any>()
    val bannerList = _bannerList.switchMap { HomeRepository.getBanner() }

    // 首页列表数据 Flow
    val getArticlesFlow = HomeRepository.getArticlePageList(20).cachedIn(viewModelScope)    // cachedIn()操作符使数据流可共享

    // 手动拉取首页数据
    fun getBanner() { _bannerList.value = _bannerList.value }
}