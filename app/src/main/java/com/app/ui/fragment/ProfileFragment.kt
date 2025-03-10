package com.app.ui.fragment

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.R
import com.app.databinding.FragmentProfileBinding
import com.app.ui.activity.LoginActivity
import com.app.ui.activity.profile.AddressListActivity
import com.app.ui.activity.profile.PasswordEditActivity
import com.bumptech.glide.Glide
import com.core.ui.BaseFragment
import com.core.util.openGallery
import com.presentation.ProfileFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileFragmentViewModel by viewModels()

    private var imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.setImageProfileUri(result.data?.data.toString())
        }
    }

    private val editPasswordLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.getStringExtra(getString(R.string.password_edit_key))
                ?: return@registerForActivityResult
            viewModel.setPassword(data)
        }
    }

    private val editAddressLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val position = result.data?.getIntExtra(getString(R.string.address_position_key), -1)
                ?: return@registerForActivityResult
            viewModel.setAddress(position)
        }
    }

    override fun setUi() {
        binding.viewModel = viewModel
        bindingOnClick()
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.profileImageUrl.observe(lifecycleOwner, ::setProfileImage)
        viewModel.successUerInfoEdit.observe(lifecycleOwner, ::offEditMode)
    }

    private fun bindingOnClick() {
        binding.btnEditProfile.setOnClickListener {
            onEditMode()
        }
        binding.layoutOpenGallery.setOnClickListener {
            openGallery(imagePickerLauncher)
        }
        binding.imgProfile.setOnClickListener {
            openGallery(imagePickerLauncher)
        }
        binding.layoutEditPassword.setOnClickListener {
            startEditPasswordActivity()
        }
        binding.layoutEditAddress.setOnClickListener {
            startEditAddressActivity()
        }
        binding.btnSignOut.setOnClickListener {
            viewModel.trySignOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun setProfileImage(uri: String) {
        if (uri.isEmpty()) return
        Glide.with(binding.root).load(uri).into(binding.imgProfile)
    }

    private fun onEditMode() {
        binding.btnEditProfile.visibility = View.GONE
        binding.btnCompleteProfile.visibility = View.VISIBLE
        binding.layoutOpenGallery.visibility = View.VISIBLE
        binding.layoutEditPassword.visibility = View.VISIBLE
        binding.layoutEditAddress.visibility = View.VISIBLE

        binding.editNickName.isFocusableInTouchMode = true
        binding.editStatusMessage.isFocusableInTouchMode = true
        binding.editStatusMessage.isFocusableInTouchMode = true
        binding.editName.isFocusableInTouchMode = true
        binding.editPhone.isFocusableInTouchMode = true
    }

    private fun offEditMode(status: Boolean) {
        if (!status) return
        binding.btnEditProfile.visibility = View.VISIBLE
        binding.btnCompleteProfile.visibility = View.GONE
        binding.layoutOpenGallery.visibility = View.GONE
        binding.layoutEditPassword.visibility = View.GONE
        binding.layoutEditAddress.visibility = View.GONE

        binding.editNickName.isFocusable = false
        binding.editStatusMessage.isFocusable = false
        binding.editName.isFocusable = false
        binding.editPhone.isFocusable = false
    }

    private fun startEditPasswordActivity() {
        val intent = Intent(requireContext(), PasswordEditActivity::class.java)
        editPasswordLauncher.launch(intent)
    }

    private fun startEditAddressActivity() {
        val intent = Intent(requireContext(), AddressListActivity::class.java)
        editAddressLauncher.launch(intent)
    }
}