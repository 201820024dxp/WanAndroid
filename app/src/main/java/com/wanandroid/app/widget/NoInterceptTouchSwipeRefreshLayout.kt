package com.wanandroid.app.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class NoInterceptTouchSwipeRefreshLayout(context: Context, attrs: AttributeSet?) :
    SwipeRefreshLayout(context, attrs) {

    private var startX = 0f
    private var startY = 0f
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x
                startY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = Math.abs(ev.x - startX)
                val dy = Math.abs(ev.y - startY)
                // 横向滑动不拦截，纵向滑动交给SwipeRefreshLayout处理
                if (dx > touchSlop) {
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}