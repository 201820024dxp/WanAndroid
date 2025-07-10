package com.wanandroid.app.ui.home.child.explore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.wanandroid.app.logic.model.Banner
import com.wanandroid.app.logic.repository.HomeRepository
import kotlinx.coroutines.launch

class ExploreViewModel : ViewModel() {

    // 首页 Banner LiveData
    private var _bannerList = MutableLiveData<List<Banner>>()
    val bannerList get() = _bannerList

    // 首页列表数据 Flow
    val getArticlesFlow = HomeRepository.getArticlePageList(20).cachedIn(viewModelScope)    // cachedIn()操作符使数据流可共享

    fun getBanner() {
        viewModelScope.launch {
            try {
                val banners = HomeRepository.getBanner().data ?: emptyList()
                _bannerList.value = banners
            } catch (e: Exception) {
                throw RuntimeException("获取Banner失败", e)
            }
        }
    }

}