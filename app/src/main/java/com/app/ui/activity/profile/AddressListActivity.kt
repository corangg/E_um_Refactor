package com.app.ui.activity.profile

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.databinding.ActivityAddressListBinding
import com.app.ui.adapter.AddressListAdapter
import com.app.ui.custom.showCustomToast
import com.core.ui.BaseActivity
import com.domain.model.AddressItemData
import com.presentation.AddressListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressListActivity : BaseActivity<ActivityAddressListBinding>(ActivityAddressListBinding::inflate) {
    private val viewModel: AddressListViewModel by viewModels()
    private val adapter by lazy { AddressListAdapter() }
    override fun setUi() {
        bindingRecyclerView()
        bindingOnClick()
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.addressListData.observe(lifecycleOwner, ::updateAddressList)
        viewModel.messageType.observe(lifecycleOwner, ::showMessage)
    }

    private fun bindingOnClick() {
        binding.btnBackActivity.setOnClickListener { finish() }
        binding.btnEditAddress.setOnClickListener { adapter.onEditMode() }
        binding.btnAddAddress.setOnClickListener { startAddressManagementActivity() }
    }

    private fun bindingRecyclerView() {
        binding.recyclerAddressList.apply {
            layoutManager = LinearLayoutManager(this@AddressListActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@AddressListActivity.adapter
        }

        adapter.setOnItemClickListener { item, position ->
            val resultIntent = Intent()
            resultIntent.putExtra(getString(R.string.address_position_key), position)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
        adapter.setOnItemEditListener {
            startAddressManagementActivity(it)
        }
        adapter.setOnItemDeleteListener {
            viewModel.deleteAddress(it)
        }
    }

    private fun updateAddressList(list: List<AddressItemData>) {
        adapter.submitList(list)
    }

    private fun startAddressManagementActivity(position: Int = -1) {
        val intent = Intent(this, AddressManagementActivity::class.java)
        intent.putExtra(getString(R.string.address_management_key), position)
        startActivity(intent)
    }

    private fun showMessage(type: Int) {
        when (type) {
            1 -> showCustomToast(getString(R.string.address_toast_1))
        }
    }
}