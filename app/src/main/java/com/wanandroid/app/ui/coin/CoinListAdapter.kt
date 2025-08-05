package com.wanandroid.app.ui.coin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ItemCoinInfoListLayoutBinding
import com.wanandroid.app.logic.model.CoinHistory

class CoinListAdapter(val context: Context, diffCallback: DiffUtil.ItemCallback<CoinHistory>)
    : PagingDataAdapter<CoinHistory, CoinListAdapter.ViewHolder>(diffCallback) {

    object CoinHistoryDiffCallback : DiffUtil.ItemCallback<CoinHistory>() {
        override fun areItemsTheSame(oldItem: CoinHistory, newItem: CoinHistory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CoinHistory, newItem: CoinHistory): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(binding: ItemCoinInfoListLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        val reason = binding.coinReason
        val desc = binding.coinDescription
        val count = binding.coinCount
        val bLine = binding.coinBottomLine
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCoinInfoListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.reason.text = item.reason
            holder.desc.text = item.desc
            holder.count.text = context.getString(R.string.add_coin_count, item.coinCount.toString())
        }
        holder.bLine.isVisible = (position != itemCount - 1) // 最后一项不显示底部分割线
    }
}