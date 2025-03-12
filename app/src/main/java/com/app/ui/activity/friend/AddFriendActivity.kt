package com.app.ui.activity.friend

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.R
import com.app.databinding.ActivityAddFriendBinding
import com.app.ui.custom.showCustomToast
import com.bumptech.glide.Glide
import com.core.ui.BaseActivity
import com.presentation.AddFriendActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFriendActivity :
    BaseActivity<ActivityAddFriendBinding>(ActivityAddFriendBinding::inflate) {
    private val viewModel: AddFriendActivityViewModel by viewModels()

    override fun setUi() {
        binding.viewModel = viewModel
        bindingOnClick()
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.onShowRequestView.observe(lifecycleOwner, ::showRequestView)
        viewModel.profileImageUrl.observe(lifecycleOwner, ::setProfileImage)
        viewModel.onFriendRequestValue.observe(lifecycleOwner, ::requestFriend)
    }

    private fun bindingOnClick() {
        binding.btnBackActivity.setOnClickListener { finish() }
    }

    private fun showRequestView(result: Boolean) {
        if (result) {
            binding.requestGroup.visibility = View.VISIBLE
        }
    }

    private fun setProfileImage(uri: String) {
        if (uri.isEmpty()) return
        Glide.with(binding.root).load(uri).into(binding.imgProfile)
    }

    private fun requestFriend(value: Int) {
        when (value) {
            1 -> {
                this.showCustomToast(getString(R.string.add_friend_request_toast_1))
                finish()
            }

            2 -> this.showCustomToast(getString(R.string.add_friend_request_toast_2))
        }
    }
}