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
import com.data.config.REQUEST_CODE
import com.data.config.RESPONSE_CODE
import com.domain.model.AlarmData

class AlarmAdapter : BaseRecyclerView<AlarmData, BaseViewHolder<AlarmData>>(object :
        DiffUtil.ItemCallback<AlarmData>() {
        override fun areItemsTheSame(
            oldItem: AlarmData,
            newItem: AlarmData
        ) = oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(
            oldItem: AlarmData,
            newItem: AlarmData
        ) = oldItem == newItem
    }) {
    private lateinit var mContext: Context
    private var selectedPosition = 0

    private var onItemRequestListener: ((Int, Boolean) -> Unit)? = null
    private var onItemResponseListener: ((Int) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AlarmData.RequestFriendAlarmData -> REQUEST_CODE
            is AlarmData.ResponseFriendAlarmData -> RESPONSE_CODE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<AlarmData> {
        mContext = parent.context
        return when (viewType) {
            REQUEST_CODE -> RequestFriendAlarmViewHolder(
                ItemRequestFriendAlarmBinding.inflate(
                    LayoutInflater.from(mContext), parent, false
                )
            )

            RESPONSE_CODE -> ResponseFriendAlarmViewHolder(
                ItemResponseFriendAlarmBinding.inflate(
                    LayoutInflater.from(mContext), parent, false
                )
            )

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<AlarmData>, position: Int) {
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
    ) : BaseViewHolder<AlarmData>(binding) {

        override fun bind(
            item: AlarmData,
            position: Int,
            clickListener: ((AlarmData, Int) -> Unit)?
        ) {
            if (item is AlarmData.RequestFriendAlarmData) {
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
    ) : BaseViewHolder<AlarmData>(binding) {

        override fun bind(
            item: AlarmData,
            position: Int,
            clickListener: ((AlarmData, Int) -> Unit)?
        ) {
            if (item is AlarmData.ResponseFriendAlarmData) {
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