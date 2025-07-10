package com.wanandroid.app.ui.home.child.square

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.wanandroid.app.logic.repository.HomeRepository

class SquareViewModel : ViewModel() {

    val getSquareFlow = HomeRepository.getSquareArticlePageList(20).cachedIn(viewModelScope)

}