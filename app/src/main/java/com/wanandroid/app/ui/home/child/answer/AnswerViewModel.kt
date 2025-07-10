package com.wanandroid.app.ui.home.child.answer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.wanandroid.app.logic.repository.HomeRepository

class AnswerViewModel : ViewModel() {

    val getAnswerFlow = HomeRepository.getAnswerPageList(20).cachedIn(viewModelScope)

}