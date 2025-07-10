package com.wanandroid.app.ui.search.begin

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ItemTextViewChipBinding
import com.wanandroid.app.logic.model.HotKey
import com.wanandroid.app.ui.search.SearchActivity

class SearchHotKeyAdapter(val activity: SearchActivity, val hotKeyList: List<HotKey>) :
    RecyclerView.Adapter<SearchHotKeyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chipLayout = view.findViewById<MaterialCardView>(R.id.chipLayout)
        val chipTextView = view.findViewById<TextView>(R.id.chipTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_text_view_chip, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = hotKeyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hotKey = hotKeyList[position]
        holder.chipTextView.text = hotKey.name
        holder.itemView.setOnClickListener {
            Log.d("SearchHotKeyAdapter", "Clicked on hot key: ${hotKey.name}")
            activity.setSearchEditText(hotKey.name)
            activity.search(hotKey.name)
        }
    }


}