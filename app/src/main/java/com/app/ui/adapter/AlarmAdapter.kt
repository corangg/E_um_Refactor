package com.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.app.R
import com.app.databinding.ItemRequestFriendAlarmBinding
import com.app.databinding.ItemResponseFriendAlarmBinding
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder
import com.data.config.FRIEND_REQUEST_CODE
import com.data.config.FRIEND_RESPONSE_CODE
import com.domain.model.FriendAlarmData

class AlarmAdapter : BaseRecyclerView<FriendAlarmData, BaseViewHolder<FriendAlarmData>>(object :
        DiffUtil.ItemCallback<FriendAlarmData>() {
        override fun areItemsTheSame(
            oldItem: FriendAlarmData,
            newItem: FriendAlarmData
        ) = oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(
            oldItem: FriendAlarmData,
            newItem: FriendAlarmData
        ) = oldItem == newItem
    }) {
    private lateinit var mContext: Context
    private var selectedPosition = 0

    private var onItemRequestListener: ((Int, Boolean) -> Unit)? = null
    private var onItemResponseListener: ((Int) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FriendAlarmData.RequestFriendFriendAlarmData -> FRIEND_REQUEST_CODE
            is FriendAlarmData.ResponseFriendFriendAlarmData -> FRIEND_RESPONSE_CODE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<FriendAlarmData> {
        mContext = parent.context
        return when (viewType) {
            FRIEND_REQUEST_CODE -> RequestFriendAlarmViewHolder(
                ItemRequestFriendAlarmBinding.inflate(
                    LayoutInflater.from(mContext), parent, false
                )
            )

            FRIEND_RESPONSE_CODE -> ResponseFriendAlarmViewHolder(
                ItemResponseFriendAlarmBinding.inflate(
                    LayoutInflater.from(mContext), parent, false
                )
            )

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<FriendAlarmData>, position: Int) {
        super.onBindViewHolder(holder, position)
        when (holder) {
            is RequestFriendAlarmViewHolder -> holder.addBind(position, onItemRequestListener)
            is ResponseFriendAlarmViewHolder -> holder.addBind(position, onItemResponseListener)
        }
    }

    fun setOnItemRequestListener(listener: (Int, Boolean) -> Unit) {
        onItemRequestListener = listener
    }

    fun setOnItemResponseListener(listener: (Int) -> Unit) {
        onItemResponseListener = listener
    }


    inner class RequestFriendAlarmViewHolder(
        private val binding: ItemRequestFriendAlarmBinding
    ) : BaseViewHolder<FriendAlarmData>(binding) {

        override fun bind(
            item: FriendAlarmData,
            position: Int,
            clickListener: ((FriendAlarmData, Int) -> Unit)?
        ) {
            if (item is FriendAlarmData.RequestFriendFriendAlarmData) {
                val defaultMessage = mContext.getString(R.string.add_friend_request_alarm_item_message)
                val message = "${item.nickName} $defaultMessage"
                binding.textMessage.text = message
            }
        }

        fun addBind(
            position: Int,
            responseListener: ((Int, Boolean) -> Unit)?
        ) {
            binding.btnOk.setOnClickListener {
                responseListener?.invoke(position, true)
            }

            binding.btnNo.setOnClickListener {
                responseListener?.invoke(position, false)
            }
        }
    }

    inner class ResponseFriendAlarmViewHolder(
        private val binding: ItemResponseFriendAlarmBinding
    ) : BaseViewHolder<FriendAlarmData>(binding) {

        override fun bind(
            item: FriendAlarmData,
            position: Int,
            clickListener: ((FriendAlarmData, Int) -> Unit)?
        ) {
            if (item is FriendAlarmData.ResponseFriendFriendAlarmData) {
                val defaultMessage = mContext.getString(R.string.add_friend_response_alarm_item_message)
                val message = "${item.nickName} $defaultMessage"
                binding.textMessage.text = message
            }
        }

        fun addBind(
            position: Int,
            responseListener: ((Int) -> Unit)?
        ) {
            binding.btnResponseOk.setOnClickListener {
                responseListener?.invoke(position)
            }
        }
    }
}