package com.app.ui.fragment

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.databinding.FragmentScheduleBinding
import com.app.ui.activity.ScheduleActivity
import com.app.ui.activity.friend.RequestFriendAlarmActivity
import com.app.ui.adapter.ScheduleListAdapter
import com.core.ui.BaseFragment
import com.domain.model.ScheduleActivityType
import com.domain.model.ScheduleData
import com.presentation.ScheduleFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(FragmentScheduleBinding::inflate) {
    val viewModel: ScheduleFragmentViewModel by viewModels()
    private val adapter by lazy { ScheduleListAdapter() }


    override fun setUi() {
        bindingRecyclerView()
        bindingOnClick()
    }

    override fun setUpDate() {}

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.scheduleListData.observe(lifecycleOwner, ::updateScheduleList)
        viewModel.alarmListLiveData.observe(lifecycleOwner,::setFriendRequestAlarm)
    }

    private fun bindingOnClick() {
        binding.btnAlarm.setOnClickListener {
            val intent = Intent(requireContext(), RequestFriendAlarmActivity::class.java)
            startActivity(intent)
        }
    }

    private fun bindingRecyclerView() {
        binding.recyclerSchedule.apply { layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@ScheduleFragment.adapter
        }

        adapter.setOnItemClickListener { item, position ->
            setScheduleData(item)
        }
    }

    private fun updateScheduleList(list: List<ScheduleData>) {
        adapter.submitList(list)
    }

    private fun setFriendRequestAlarm(alarmSize: Int) {
        binding.viewAlarm.visibility = if (alarmSize > 0) View.VISIBLE else View.GONE
    }

    private fun setScheduleData(data: ScheduleData){
        val intent = Intent(requireContext(), ScheduleActivity::class.java)
        intent.putExtra(getString(R.string.schedule_extra_date_key), data.time)
        /*intent.putExtra(getString(R.string.schedule_extra_email_key), data.email)
        intent.putExtra(getString(R.string.schedule_extra_schedule_address_key), data.scheduleAddress)
        intent.putExtra(getString(R.string.schedule_extra_alarm_key), data.alarmTime)
        intent.putExtra(getString(R.string.schedule_extra_transport_type_key), data.transportType)
        intent.putExtra(getString(R.string.schedule_extra_type_key), ScheduleActivityType.Edit.type)*/
        startActivity(intent)
    }
}