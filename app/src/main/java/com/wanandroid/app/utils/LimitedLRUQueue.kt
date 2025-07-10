package com.wanandroid.app.utils

/**
 * 用于保存搜索历史，采用LRU（最近最少使用）缓存策略
 */
class LimitedLRUQueue<T>(private val maxSize: Int) : ArrayList<T>() {

    override fun add(element: T): Boolean {
        if (contains(element)) {
            remove(element)
        } else if (size >= maxSize) {
            removeAt(0) // 移除最旧的元素
        }
        return super.add(element)
    }

    override fun add(index: Int, element: T) {
        if (contains(element)) {
            remove(element)
        } else if (size >= maxSize) {
            removeAt(0) // 移除最旧的元素
        }
        super.add(index, element)
    }

    override fun get(index: Int): T {
        if (index < 0 || index >= size) {
            throw IndexOutOfBoundsException("Index: $index, Size: $size")
        }
        return super.get(index)
    }

}