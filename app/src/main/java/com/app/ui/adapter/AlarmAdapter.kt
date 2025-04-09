package com.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.app.R
import com.app.databinding.ItemRequestFriendAlarmBinding
import com.app.databinding.ItemRequestScheduleAlarmBinding
import com.app.databinding.ItemResponseFriendAlarmBinding
import com.core.recyclerview.BaseRecyclerView
import com.core.recyclerview.BaseViewHolder
import com.data.config.FRIEND_REQUEST_CODE
import com.data.config.FRIEND_RESPONSE_CODE
import com.data.config.SCHEDULE_REQUEST_CODE
import com.data.config.SCHEDULE_RESPONSE_CODE
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
    private var onItemScheduleRequestListener: ((Int, Boolean, AlarmData) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AlarmData.RequestFriendAlarmData -> FRIEND_REQUEST_CODE
            is AlarmData.ResponseFriendAlarmData -> FRIEND_RESPONSE_CODE
            is AlarmData.RequestScheduleAlarmData -> SCHEDULE_REQUEST_CODE
            is AlarmData.ResponseScheduleAlarmData -> SCHEDULE_RESPONSE_CODE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<AlarmData> {
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

            SCHEDULE_REQUEST_CODE -> RequestScheduleAlarmViewHolder(
                ItemRequestScheduleAlarmBinding.inflate(
                    LayoutInflater.from(mContext), parent, false
                )
            )

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<AlarmData>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        when (holder) {
            is RequestFriendAlarmViewHolder -> holder.addBind(position, onItemRequestListener)
            is ResponseFriendAlarmViewHolder -> holder.addBind(position, onItemResponseListener)
            is RequestScheduleAlarmViewHolder -> holder.addBind(item,position, onItemScheduleRequestListener)
        }
    }

    fun setOnItemRequestListener(listener: (Int, Boolean) -> Unit) {
        onItemRequestListener = listener
    }

    fun setOnItemResponseListener(listener: (Int) -> Unit) {
        onItemResponseListener = listener
    }

    fun setOnItemScheduleRequestListener(listener: (Int, Boolean, AlarmData) -> Unit){
        onItemScheduleRequestListener = listener
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

    inner class RequestScheduleAlarmViewHolder(
        private val binding: ItemRequestScheduleAlarmBinding
    ) : BaseViewHolder<AlarmData>(binding) {

        override fun bind(
            item: AlarmData,
            position: Int,
            clickListener: ((AlarmData, Int) -> Unit)?
        ) {
            if (item is AlarmData.RequestScheduleAlarmData) {
                val defaultMessage = mContext.getString(R.string.schedule_request_alarm_item_message)
                val message = "${item.nickName} $defaultMessage"
                binding.textMessage.text = message
            }
        }

        fun addBind(
            item: AlarmData,
            position: Int,
            requestListener: ((Int, Boolean,AlarmData) -> Unit)?
        ) {
            binding.btnOk.setOnClickListener {
                requestListener?.invoke(position, true, item)
            }

            binding.btnNo.setOnClickListener {
                requestListener?.invoke(position, true, item)
            }
        }
    }
}