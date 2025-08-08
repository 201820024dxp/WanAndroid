package com.wanandroid.app.ui.navigation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import kotlin.math.abs


class InterceptLinearLayout : LinearLayout {
    private var downX = 0f
    private var downY = 0f
    private var disallowIntercept = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                disallowIntercept = false
                parent.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = abs((ev.x - downX).toDouble()).toFloat()
                val dy = abs((ev.y - downY).toDouble()).toFloat()
                if (dx > dy) {
                    // 横向滑动，拦截父 ViewPager2
                    parent.requestDisallowInterceptTouchEvent(true)
                } else {
                    // 纵向滑动，不干涉
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
        }
        return false
    }
}
