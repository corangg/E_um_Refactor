package com.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.app.databinding.ItemChatRoomListBinding
import com.bumptech.glide.Glide
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder
import com.domain.model.ChatRoomItemData

class ChatRoomAdapter :
    BaseRecyclerView<ChatRoomItemData, ChatRoomAdapter.ChatRoomViewHolder>(object :
        DiffUtil.ItemCallback<ChatRoomItemData>() {
        override fun areItemsTheSame(
            oldItem: ChatRoomItemData,
            newItem: ChatRoomItemData
        ) = oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(
            oldItem: ChatRoomItemData,
            newItem: ChatRoomItemData
        ) = oldItem == newItem
    }) {
    private lateinit var mContext: Context
    private var selectedPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatRoomViewHolder {
        mContext = parent.context
        return ChatRoomViewHolder(
            ItemChatRoomListBinding.inflate(
                LayoutInflater.from(mContext),
                parent,
                false
            )
        )
    }

    inner class ChatRoomViewHolder(
        private val binding: ItemChatRoomListBinding
    ) : BaseViewHolder<ChatRoomItemData>(binding) {

        override fun bind(
            item: ChatRoomItemData,
            position: Int,
            clickListener: ((ChatRoomItemData, Int) -> Unit)?
        ) {
            binding.textItemNickname.text = item.name
            binding.textItemLastMassage.text = item.lastMessage
            Glide.with(binding.root).load(item.imgUrl).into(binding.imgProfile)
            if (item.notReadCount > 0) {
                binding.textChatAlarm.visibility = View.VISIBLE
                binding.textChatAlarm.text = "${item.notReadCount}"
            } else {
                binding.textChatAlarm.visibility = View.GONE
            }
            binding.itemChatRoom.setOnClickListener {
                clickListener?.invoke(item, position)
            }
        }
    }
}