package com.wanandroid.app.ui.profile

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.wanandroid.app.databinding.ItemProfileTagLayoutBinding
import com.wanandroid.app.logic.model.ProfileItemBean
import com.wanandroid.app.ui.share.ShareActivity

class ProfileItemAdapter(val profileList: List<ProfileItemBean>) :
    RecyclerView.Adapter<ProfileItemAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemProfileTagLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val icon = binding.profileItemIcon
        val title = binding.profileItemTitle
        val badge = binding.profileItemBadge
        val messCount = binding.profileItemMessageCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(
            ItemProfileTagLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            val profileItem = profileList[position]
            // TODO: profile item 点击事件
            val tempContext = holder.itemView.context
            when (profileItem.title) {
                ProfileFragment.SHARE_ARTICLE -> {
                    tempContext.startActivity(Intent(tempContext, ShareActivity::class.java))
                }

                ProfileFragment.MY_SHARE -> {}
                ProfileFragment.MY_COLLECT -> {}
                ProfileFragment.TOOL_LIST -> {}
            }
            Log.d(this.javaClass.simpleName, "you click ${profileItem.title}")
        }
        return holder
    }

    override fun getItemCount(): Int = profileList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profileItem = profileList[position]
        holder.icon.setImageResource(profileItem.iconResource)
        holder.title.text = profileItem.title
        if (position == 0 && profileItem.badge.type == ProfileItemBean.Badge.BadgeType.NUMBER
            && profileItem.badge.messageCount > 0
        ) {
            holder.badge.isVisible = true
            holder.messCount.text = profileItem.badge.messageCount.toString()
        } else {
            holder.badge.isVisible = false
        }
    }

}