package com.wanandroid.app.ui.coin.rank

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ItemCoinRankingListLayoutBinding
import com.wanandroid.app.logic.model.CoinInfo

class RankListAdapter(val context: Context, diffCallback: DiffUtil.ItemCallback<CoinInfo>)
    : PagingDataAdapter<CoinInfo, RankListAdapter.ViewHolder>(diffCallback) {

    object RankListDiffCallback : DiffUtil.ItemCallback<CoinInfo>() {
        override fun areItemsTheSame(oldItem: CoinInfo, newItem: CoinInfo): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: CoinInfo, newItem: CoinInfo): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(binding: ItemCoinRankingListLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        val rank = binding.coinRank
        val uName = binding.coinUserName
        val level = binding.coinUserLevel
        val count = binding.coinUserCount
        val bLine = binding.coinRankingBottomLine
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCoinRankingListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coinInfo = getItem(position)
        if (coinInfo != null) {
            holder.rank.text = coinInfo.rank
            holder.uName.text = coinInfo.username
            holder.level.text = context.getString(R.string.level_of, coinInfo.level.toString())
            holder.count.text = coinInfo.coinCount.toString()
        }
        holder.bLine.isVisible = (position != itemCount - 1)    // 最后一个item不显示下划线
    }
}