package com.wanandroid.app.ui.coin.rank

import androidx.lifecycle.ViewModel
import com.wanandroid.app.logic.repository.CoinRepository

class RankViewModel : ViewModel() {

    val rankList = CoinRepository.getCoinRankList()

}