package com.wanandroid.app.ui.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.wanandroid.app.logic.repository.CoinRepository

class CoinViewModel : ViewModel() {

    val selfCoinInfoLiveData = liveData{ emit(CoinRepository.getSelfCoinInfo()) }

    val coinHistoryListFlow = CoinRepository.getCoinHistoryList()
}