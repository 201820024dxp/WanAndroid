package com.wanandroid.app.ui.home.child.explore

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanandroid.app.http.getOrNull
import com.wanandroid.app.logic.model.BannerResponse
import com.wanandroid.app.logic.repository.HomeRepository
import kotlinx.coroutines.launch

class ExploreViewModel : ViewModel() {

    private var _bannerList = MutableLiveData<List<BannerResponse.Banner>>()
    val bannerList get() = _bannerList

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