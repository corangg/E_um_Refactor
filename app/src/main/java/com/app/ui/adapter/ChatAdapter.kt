package com.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.app.databinding.ItemChatReceiveBinding
import com.app.databinding.ItemChatReceiveProfileBinding
import com.app.databinding.ItemChatSendBinding
import com.bumptech.glide.Glide
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder
import com.data.config.RECEIVE_CODE
import com.data.config.RECEIVE_PROFILE_CODE
import com.data.config.SEND_CODE
import com.domain.model.ChatMessageData

class ChatAdapter :
    BaseRecyclerView<ChatMessageData, BaseViewHolder<ChatMessageData>>(object :
        DiffUtil.ItemCallback<ChatMessageData>() {
        override fun areItemsTheSame(
            oldItem: ChatMessageData,
            newItem: ChatMessageData
        ) = oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(
            oldItem: ChatMessageData,
            newItem: ChatMessageData
        ) = oldItem == newItem
    }) {
    private lateinit var mContext: Context
    private var selectedPosition = 0

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).messageType) {
            1 -> SEND_CODE
            2 -> RECEIVE_CODE
            3 -> RECEIVE_PROFILE_CODE
            else -> -1
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ChatMessageData> {
        mContext = parent.context
        return when (viewType) {
            SEND_CODE -> SendChatViewHolder(
                ItemChatSendBinding.inflate(
                    LayoutInflater.from(mContext), parent, false
                )
            )

            RECEIVE_PROFILE_CODE -> ReceiveProfileChatViewHolder(
                ItemChatReceiveProfileBinding.inflate(
                    LayoutInflater.from(mContext), parent, false
                )
            )

            RECEIVE_CODE -> ReceiveChatViewHolder(
                ItemChatReceiveBinding.inflate(
                    LayoutInflater.from(mContext), parent, false
                )
            )

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    inner class SendChatViewHolder(
        private val binding: ItemChatSendBinding
    ) : BaseViewHolder<ChatMessageData>(binding) {

        override fun bind(
            item: ChatMessageData,
            position: Int,
            clickListener: ((ChatMessageData, Int) -> Unit)?
        ) {
            binding.textMessage.text = item.message
        }
    }

    inner class ReceiveChatViewHolder(
        private val binding: ItemChatReceiveBinding
    ) : BaseViewHolder<ChatMessageData>(binding) {

        override fun bind(
            item: ChatMessageData,
            position: Int,
            clickListener: ((ChatMessageData, Int) -> Unit)?
        ) {
            binding.textMessage.text = item.message
        }
    }

    inner class ReceiveProfileChatViewHolder(
        private val binding: ItemChatReceiveProfileBinding
    ) : BaseViewHolder<ChatMessageData>(binding) {

        override fun bind(
            item: ChatMessageData,
            position: Int,
            clickListener: ((ChatMessageData, Int) -> Unit)?
        ) {
            Glide.with(binding.root).load(item.imgUrl).into(binding.imgProfile)
            binding.textItemNickname.text = item.nickname
            binding.textMessage.text = item.message
        }
    }
}