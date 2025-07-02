package com.wanandroid.app.base

import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * 通用PagingSource类
 */
class IntKeyPagingSource<VALUE : Any> (
    private val pageStart: Int = 1,
    private val block: suspend () -> List<VALUE>
) : PagingSource<Int, VALUE>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VALUE> {
        try {
            // Start refresh at page 1 if undefined.
            val page = params.key ?: pageStart
            val response = block()
            return LoadResult.Page(
                data = response,
                prevKey = if (page == pageStart) null else page - 1,    // 不是第一页就 - 1
                nextKey = if (response.isEmpty()) null else page + 1    // 还有下一页就 + 1
            )
        } catch (e: Exception) {
            // Handle errors in this block and return LoadResult.Error for
            // expected errors (such as a network failure).
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, VALUE>): Int? {
        // Try to find the page key of the closest page to anchorPosition from
        // either the prevKey or the nextKey; you need to handle nullability
        // here.
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey are null -> anchorPage is the
        //    initial page, so return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}