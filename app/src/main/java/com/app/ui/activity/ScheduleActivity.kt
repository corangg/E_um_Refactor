package com.app.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.app.R
import com.app.databinding.ActivityScheduleBinding
import com.app.ui.custom.showCustomToast
import com.core.ui.BaseActivity
import com.core.util.setEditTextMaxValue
import com.domain.model.SelectTransportationResult
import com.presentation.ScheduleActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleActivity : BaseActivity<ActivityScheduleBinding>(ActivityScheduleBinding::inflate) {
    private val viewModel: ScheduleActivityViewModel by viewModels()

    private val getStartAddressLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val address = result.data?.getStringExtra(getString(R.string.map_search_address_key)) ?: return@registerForActivityResult
                viewModel.changeStartAddress(address)
            }
        }

    private val getScheduleAddressLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val address = result.data?.getStringExtra(getString(R.string.map_search_address_key)) ?: return@registerForActivityResult
                viewModel.changeScheduleAddress(address)
            }
        }


    override fun setUi() {
        binding.viewModel = viewModel
        bindingOnClick()
        setDate()
        setEditFilter()
    }

    override fun setUpDate() {}

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.isAmPm.observe(lifecycleOwner, ::toggleAmPm)
        viewModel.textScheduleLocation.observe(lifecycleOwner, ::getTravelTime)
        viewModel.selectTransportType.observe(lifecycleOwner,::selectTransport)
        viewModel.isRequestResult.observe(lifecycleOwner,::onResultRequestSchedule)
    }

    private fun bindingOnClick() {
        binding.btnBackActivity.setOnClickListener { finish() }
        binding.textStartLocation.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra(
                getString(R.string.map_extra_start_address_key),
                viewModel.textStartLocation.value
            )
            getStartAddressLauncher.launch(intent)
        }
        binding.textScheduleLocation.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            getScheduleAddressLauncher.launch(intent)
        }
        binding.btnRequest.setOnClickListener {
            val email = intent.getStringExtra(getString(R.string.schedule_extra_email_key))?: return@setOnClickListener
            viewModel.requestSchedule(email)
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

    private fun setEditFilter(){
        setEditTextMaxValue(binding.editHour, 12)
        setEditTextMaxValue(binding.editMinute, 59)
        setEditTextMaxValue(binding.editAlarmHour, 11)
        setEditTextMaxValue(binding.editAlarmMinute, 59)
    }

    private fun getTravelTime(location: String) {
        if (location != "") {
            viewModel.setTransportTime()
        }
    }

    private fun selectTransport(type: Int) {
        val selectColor = ContextCompat.getColor(this, R.color.theme_color)
        val unSelectColor = ContextCompat.getColor(this, R.color.btn_gray)
        val walkSelected = type == SelectTransportationResult.Walk.code
        val busSelected = type == SelectTransportationResult.Car.code
        val carSelected = type == SelectTransportationResult.PublicTransport.code

        binding.imgWalk.setColorFilter(if (walkSelected) selectColor else unSelectColor)
        binding.textWalk.setTextColor(if (walkSelected) selectColor else unSelectColor)

        binding.imgBus.setColorFilter(if (busSelected) selectColor else unSelectColor)
        binding.textBus.setTextColor(if (busSelected) selectColor else unSelectColor)

        binding.imgCar.setColorFilter(if (carSelected) selectColor else unSelectColor)
        binding.textCar.setTextColor(if (carSelected) selectColor else unSelectColor)
    }

    private fun onResultRequestSchedule(result: Boolean){
        if(result){
            showCustomToast(getString(R.string.schedule_request_toast_1))
            finish()
        }else{
            showCustomToast(getString(R.string.schedule_request_toast_2))
        }
    }
}