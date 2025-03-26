package com.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.app.databinding.ItemSearchListBinding
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder
import com.domain.model.PlaceItem

class SearchListAdapter :
    BaseRecyclerView<PlaceItem, SearchListAdapter.SearchListViewHolder>(object :
        DiffUtil.ItemCallback<PlaceItem>() {
        override fun areItemsTheSame(
            oldItem: PlaceItem,
            newItem: PlaceItem
        ) = oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(
            oldItem: PlaceItem,
            newItem: PlaceItem
        ) = oldItem == newItem
    }) {
    private lateinit var mContext: Context
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListViewHolder {
        mContext = parent.context
        return SearchListViewHolder(
            ItemSearchListBinding.inflate(
                LayoutInflater.from(mContext), parent, false
            )
        )
    }

    inner class SearchListViewHolder(
        private val binding: ItemSearchListBinding
    ) : BaseViewHolder<PlaceItem>(binding) {

        override fun bind(
            item: PlaceItem,
            position: Int,
            clickListener: ((PlaceItem, Int) -> Unit)?
        ) {
            binding.textItemTitle.text = item.title
            binding.textItemAddress.text = item.address
            val distance = item.distance

            if (distance >= 1000) {
                val km = "${(distance / 1000).toInt()}km"
                binding.textItemDistance.text = km
            } else {
                val m = "${distance.toInt()}m"
                binding.textItemDistance.text = m
            }
            binding.itemSearch.setOnClickListener {
                clickListener?.invoke(item, position)
            }
        }
    }
}