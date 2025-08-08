package com.wanandroid.app.ui.navigation.widget

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import kotlin.math.abs

object ViewPager2NestedHelper {

    @SuppressLint("ClickableViewAccessibility")
    fun bindNestedScroll(
        innerViewPager2: ViewPager2
    ) {
        val recyclerView = innerViewPager2.getChildAt(0) as? RecyclerView ?: return
        recyclerView.addOnItemTouchListener(NestedTouchInterceptor(innerViewPager2))
    }

    private class NestedTouchInterceptor(
        private val innerViewPager2: ViewPager2
    ) : RecyclerView.OnItemTouchListener {

        private var startX = 0f
        private var startY = 0f
        private var isDragging = false

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            Log.d("ViewPager2NestedHelper", "onInterceptTouchEvent run")
            // 默认让父不拦截，后续根据需要再决定
            rv.parent.requestDisallowInterceptTouchEvent(true)

            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    startX = e.x
                    startY = e.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = e.x - startX
                    val dy = e.y - startY

                    val isHorizontal = abs(dx) > abs(dy)
                    if (isHorizontal) {
                        // 横向滑动时，禁用外层
                        if (dx > 0 && innerViewPager2.currentItem == 0) { // 内部的第一个ViewPager2左划
                            rv.parent.requestDisallowInterceptTouchEvent(false)
                        } else if (dx < 0 && innerViewPager2.currentItem ==
                            (innerViewPager2.adapter?.itemCount?.minus(1) ?: -1)) {
                            // 内部的最后一个ViewPager2右划
                            rv.parent.requestDisallowInterceptTouchEvent(false)
                        } else {
                            rv.parent.requestDisallowInterceptTouchEvent(true)
                        }
                    }
                }
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            Log.d("ViewPager2NestedHelper", "onTouchEvent run")
        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            Log.d("ViewPager2NestedHelper", "onRequestDisallowInterceptTouchEvent run")
        }
    }
}