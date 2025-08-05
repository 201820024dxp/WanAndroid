package com.wanandroid.app.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ItemFooterBinding

class RecyclerViewFooterAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<RecyclerViewFooterAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(parent: ViewGroup, retry: () -> Unit) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_footer, parent, false)
    ) {
        private val binding = ItemFooterBinding.bind(itemView)
        private val endTips = binding.endTips
        private val progressBar = binding.loadingProgress
        private val errorMsg = binding.errorMsg
        private val retry = binding.retryButton.also {
            it.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                errorMsg.text = loadState.error.localizedMessage
            }

            endTips.isVisible = loadState.endOfPaginationReached
            progressBar.isVisible = loadState is LoadState.Loading
            errorMsg.isVisible = loadState is LoadState.Error
            retry.isVisible = loadState is LoadState.Error
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(parent, retry)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return if (loadState.endOfPaginationReached) {
            true
        } else {
            super.displayLoadStateAsItem(loadState)
        }
    }
}