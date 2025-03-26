package com.app.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.app.R
import com.app.databinding.ActivityScheduleBinding
import com.core.ui.BaseActivity
import com.presentation.ScheduleActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.security.MessageDigest

@AndroidEntryPoint
class ScheduleActivity : BaseActivity<ActivityScheduleBinding>(ActivityScheduleBinding::inflate) {
    private val viewModel: ScheduleActivityViewModel by viewModels()

    private val getSearchAddressLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val address = result.data?.getStringExtra(getString(R.string.map_search_address_key)) ?: return@registerForActivityResult
            viewModel.changeStartAddress(address)
        }
    }

    override fun setUi() {
        binding.viewModel = viewModel
        bindingOnClick()
        setDate()
    }

    override fun setUpDate() {


    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.isAmPm.observe(lifecycleOwner, ::toggleAmPm)
    }

    private fun bindingOnClick() {
        binding.btnBackActivity.setOnClickListener { finish() }
        binding.textStartLocation.setOnClickListener{
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra(getString(R.string.map_extra_start_address_key),viewModel.textStartLocation.value)
            getSearchAddressLauncher.launch(intent)
        }
        binding.textScheduleLocation.setOnClickListener {
            true
        }

    }

    private fun toggleAmPm(value: Boolean) {
        if (value) {
            binding.btnAm.setBackgroundResource(R.drawable.bg_btn_toggle_on)
            binding.btnPm.setBackgroundResource(R.drawable.bg_btn_toggle_off)
            binding.btnAm.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.btnPm.setTextColor(ContextCompat.getColor(this, R.color.theme_color))
        } else {
            binding.btnPm.setBackgroundResource(R.drawable.bg_btn_toggle_on)
            binding.btnAm.setBackgroundResource(R.drawable.bg_btn_toggle_off)
            binding.btnPm.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.btnAm.setTextColor(ContextCompat.getColor(this, R.color.theme_color))
        }
    }

    private fun setDate() {
        val date = intent.getStringExtra(getString(R.string.schedule_extra_date_key)) ?: return
        viewModel.setDateText(date)
    }
}