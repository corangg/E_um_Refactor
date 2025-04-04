package com.app.ui.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.databinding.FragmentFriendsBinding
import com.app.ui.activity.ChatActivity
import com.app.ui.activity.ScheduleActivity
import com.app.ui.activity.friend.AddFriendActivity
import com.app.ui.activity.friend.RequestFriendAlarmActivity
import com.app.ui.adapter.FriendListAdapter
import com.bumptech.glide.Glide
import com.core.ui.BaseFragment
import com.core.ui.custom.ThreeButtonCustomDialog
import com.domain.model.FriendItemData
import com.presentation.FriendsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class FriendsFragment : BaseFragment<FragmentFriendsBinding>(FragmentFriendsBinding::inflate) {
    val viewModel: FriendsFragmentViewModel by viewModels()
    private val adapter by lazy { FriendListAdapter() }

    override fun setUi() {
        binding.viewModel = viewModel
        bindingRecyclerView()
        bindingOnClick()
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.friendListData.observe(lifecycleOwner, ::updateFriendList)
        viewModel.userProfileUrlLiveData.observe(lifecycleOwner, ::setProfileImage)
        viewModel.alarmListLiveData.observe(lifecycleOwner,::setFriendRequestAlarm)
    }

    private fun bindingRecyclerView() {
        binding.recyclerFriendsList.apply { layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@FriendsFragment.adapter
        }

        adapter.setOnItemClickListener { item, position ->
            showCustomDialog(item)
        }
    }

    private fun bindingOnClick() {
        binding.btnAddFriend.setOnClickListener {
            val intent = Intent(requireContext(), AddFriendActivity::class.java)
            startActivity(intent)
        }
        binding.btnAlarmFriend.setOnClickListener {
            val intent = Intent(requireContext(), RequestFriendAlarmActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateFriendList(list: List<FriendItemData>) {
        adapter.submitList(list)
    }

    private fun setFriendRequestAlarm(alarmSize: Int) {
        binding.viewFriendRequestAlarm.visibility = if (alarmSize > 0) View.VISIBLE else View.GONE
    }


    private fun setProfileImage(uri: String) {
        if (uri.isEmpty()) return
        Glide.with(binding.root).load(uri).into(binding.imgProfile)
    }

    private fun showCustomDialog(data: FriendItemData) {
        val customDialog = ThreeButtonCustomDialog(requireContext(), R.layout.dialog_friend_detail, width = 0.8f, height = 0.45f)
        customDialog.setText(R.id.text_friend_detail_nickname,data.nickName)
        customDialog.setText(R.id.text_friend_detail_status,data.statusMessage)
        customDialog.setImage(R.id.img_friend_detail_Profile,data.profileUrl)

        customDialog.setButtonClickListener(R.id.btn_1) {
            viewModel.getChatCode(data.email) { code ->
                val intent = Intent(requireContext(), ChatActivity::class.java)
                intent.putExtra(getString(R.string.friend_detail_chat_extra_code_key), code)
                startActivity(intent)
            }
            customDialog.dismissDialog()
        }
        customDialog.setButtonClickListener(R.id.btn_2) {
            showDatePicker(data.email)
        }
        customDialog.setButtonClickListener(R.id.btn_3) {
            customDialog.dismissDialog()
        }

        customDialog.showDialog()
    }

    private fun showDatePicker(email: String) {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format(
                    Locale.KOREA,
                    "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay
                )
                val intent = Intent(requireContext(), ScheduleActivity::class.java)
                intent.putExtra(getString(R.string.schedule_extra_date_key), selectedDate)
                intent.putExtra(getString(R.string.schedule_extra_email_key), email)
                startActivity(intent)
            },
            year, month, day
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        datePickerDialog.show()
    }
}