package com.app.ui.activity.profile

import android.content.Intent
import android.icu.text.Transliterator.Position
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.R
import com.app.databinding.ActivityManagementAddressBinding
import com.app.ui.activity.MapActivity
import com.app.ui.custom.showCustomToast
import com.core.ui.BaseActivity
import com.presentation.AddressManagementViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressManagementActivity : BaseActivity<ActivityManagementAddressBinding>(ActivityManagementAddressBinding::inflate) {
    private val viewModel: AddressManagementViewModel by viewModels()
    private val addressResultLauncher = registerForActivityResultHandler { result ->
        if (result.resultCode == RESULT_OK) {
            val zoneCode = result.data?.getStringExtra("zoneCode") ?: ""
            val address = result.data?.getStringExtra("address") ?: ""
            viewModel.setAddress(zoneCode, address)
        }
    }
    override fun setUi() {
        binding.viewModel = viewModel
        bindingOnClick()
    }

    override fun setUpDate() {
        getAddressData(intent.getIntExtra(getString(R.string.address_management_key),-1))
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.messageType.observe(lifecycleOwner,::showMessage)
    }

    private fun bindingOnClick() {
        binding.btnSearch.setOnClickListener {
            startAddressSearchActivity()
        }
    }

    private fun getAddressData(position: Int){
        viewModel.getChoiceAddressData(position)
    }

    private fun startAddressSearchActivity() {
        val intent = Intent(this, MapActivity::class.java)
        addressResultLauncher.launch(intent)
    }

    private fun showMessage(type: Int) {
        when (type) {
            1 -> showCustomToast(getString(R.string.address_management_toast_1))
            2 -> showCustomToast(getString(R.string.address_management_toast_2))
            3 -> {finish()}
            4 -> showCustomToast(getString(R.string.address_management_toast_4))
            5 -> showCustomToast(getString(R.string.address_management_toast_5))
        }
    }
}