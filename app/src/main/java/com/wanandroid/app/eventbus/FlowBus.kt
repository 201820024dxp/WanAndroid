package com.wanandroid.app.eventbus

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object FlowBus {

    class CollectStateChangedItem(val id: Int = 0, val collect: Boolean = false)
    val collectStateFlow = MutableStateFlow<CollectStateChangedItem>(CollectStateChangedItem())

}