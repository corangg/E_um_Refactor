package com.app.ui.activity.friend

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.databinding.ActivityRequestFriendAlarmBinding
import com.app.ui.activity.ScheduleActivity
import com.app.ui.adapter.AlarmAdapter
import com.app.ui.custom.showCustomToast
import com.core.ui.BaseActivity
import com.domain.model.AlarmData
import com.domain.model.ScheduleActivityType
import com.presentation.RequestFriendAlarmViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestFriendAlarmActivity :
    BaseActivity<ActivityRequestFriendAlarmBinding>(ActivityRequestFriendAlarmBinding::inflate) {
    private val viewModel: RequestFriendAlarmViewModel by viewModels()
    private val adapter by lazy { AlarmAdapter() }

    override fun setUi() {
        bindingOnClick()
        bindingRecyclerView()
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.alarmListLiveData.observe(lifecycleOwner, ::setRecyclerViewList)
        viewModel.onResponseResult.observe(lifecycleOwner, ::showResultMessage)
    }

    private fun bindingOnClick() {
        binding.btnBackActivity.setOnClickListener { finish() }
    }

    private fun bindingRecyclerView() {
        binding.recyclerFriendRequestAlarmList.apply {
            layoutManager = LinearLayoutManager(this@RequestFriendAlarmActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@RequestFriendAlarmActivity.adapter
        }

        adapter.setOnItemRequestListener { position, value ->
            viewModel.responseFriendRequest(position, value)
        }

        adapter.setOnItemResponseListener {
            viewModel.checkFriendResponse(it)
        }

        adapter.setOnItemScheduleRequestListener{position, value, alarmData ->
            if(alarmData is AlarmData.RequestScheduleAlarmData){
                if(value){
                    val intent = Intent(this, ScheduleActivity::class.java)
                    intent.putExtra(getString(R.string.schedule_extra_date_key), alarmData.scheduleTime)
                    intent.putExtra(getString(R.string.schedule_extra_email_key), alarmData.email)
                    intent.putExtra(getString(R.string.schedule_extra_schedule_address_key), alarmData.address)
                    intent.putExtra(getString(R.string.schedule_extra_key_code_key), alarmData.time)
                    intent.putExtra(getString(R.string.schedule_extra_nickname_key), alarmData.nickName)
                    intent.putExtra(getString(R.string.schedule_extra_type_key), ScheduleActivityType.Response.type)
                    startActivity(intent)
                }else{
                    viewModel.refuseSchedule(alarmData)
                }
            }
        }

        adapter.setOnItemScheduleResponseListener {position, alarmData ->
            if(alarmData is AlarmData.ResponseScheduleAlarmData){
                viewModel.processScheduleResponse(alarmData)
            }
        }
    }

    private fun setRecyclerViewList(list: List<AlarmData>) {
        adapter.submitList(list)
    }

    private fun showResultMessage(result: Boolean){
        if(result) showCustomToast(getString(R.string.add_friend_request_alarm_toast))
    }
}