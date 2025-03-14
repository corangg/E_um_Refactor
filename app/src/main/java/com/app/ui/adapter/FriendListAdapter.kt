package com.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.app.databinding.ItemFriedsListBinding
import com.bumptech.glide.Glide
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder
import com.domain.model.FriendItemData

class FriendListAdapter :
    BaseRecyclerView<FriendItemData, FriendListAdapter.FriendListViewHolder>(object : DiffUtil.ItemCallback<FriendItemData>() {
        override fun areItemsTheSame(
            oldItem: FriendItemData,
            newItem: FriendItemData
        ) = oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(
            oldItem: FriendItemData,
            newItem: FriendItemData
        ) = oldItem == newItem
    }) {
    private lateinit var mContext: Context
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendListViewHolder {
        mContext = parent.context
        return FriendListViewHolder(
            ItemFriedsListBinding.inflate(
                LayoutInflater.from(mContext), parent, false
            )
        )
    }

    inner class FriendListViewHolder(
        private val binding: ItemFriedsListBinding
    ) : BaseViewHolder<FriendItemData>(binding) {

        override fun bind(
            item: FriendItemData,
            position: Int,
            clickListener: ((FriendItemData, Int) -> Unit)?
        ) {
            Glide.with(binding.root).load(item.profileUrl).into(binding.imgProfile)
            binding.textItemNickname.text = item.nickName
            binding.textItemStatusMassage.text = item.statusMessage
        }
    }
}