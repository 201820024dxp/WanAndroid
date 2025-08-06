package com.wanandroid.app.ui.navigation.widget

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wanandroid.app.ui.navigation.navigation.NavigationChapterAdapter

class NavigationChapterScrollListener(
    val chapterList: RecyclerView,
    val chapterAdapter: NavigationChapterAdapter
) : RecyclerView.OnScrollListener() {

    // scrollState == [RecyclerView.SCROLL_STATE_DRAGGING] 说明是用户操作
    // scrollState == [RecyclerView.SCROLL_STATE_SETTLING] 说明是自动滚动
    private var scrollState = RecyclerView.SCROLL_STATE_IDLE

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (scrollState == RecyclerView.SCROLL_STATE_IDLE) {
            scrollState = newState
        }
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            scrollState = newState
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (scrollState == RecyclerView.SCROLL_STATE_SETTLING) return // 忽略非用户操作的滚动
        // 获取右侧 顶部 分组的索引
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisible = layoutManager.findFirstVisibleItemPosition()
        // 滚动以显示出 分组对应的左侧章节列表
//        binding.navChildChapterList.scrollToPosition(firstVisible)
        // Snap left list to center
        val leftLayoutManager = chapterList.layoutManager as LinearLayoutManager
        val recyclerViewHeight = chapterList.height
        val itemView = leftLayoutManager.findViewByPosition(firstVisible)
        itemView?.let {
            val itemCenter = itemView.top + itemView.height / 2
            val rvCenter = recyclerViewHeight / 2
            val scrollBy = itemCenter - rvCenter
            chapterList.scrollBy(0, scrollBy)
        } ?: run {
            chapterList.scrollToPosition(firstVisible)
        }
        // 更新左侧章节列表的选中状态
        chapterAdapter.setSelectedIndex(firstVisible)
    }
}