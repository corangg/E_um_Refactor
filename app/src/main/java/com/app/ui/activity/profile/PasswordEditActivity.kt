package com.app.ui.activity.profile

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.R
import com.app.databinding.ActivityPasswordEditBinding
import com.app.ui.custom.showCustomToast
import com.core.ui.BaseActivity
import com.presentation.EditPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordEditActivity :
    BaseActivity<ActivityPasswordEditBinding>(ActivityPasswordEditBinding::inflate) {
    private val viewModel: EditPasswordViewModel by viewModels()

    override fun setUi() {
        binding.viewModel = viewModel
        bindingOnClick()
    }

    override fun setUpDate() {

    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.passwordEditResult.observe(lifecycleOwner, ::showPasswordEditResultMessage)
    }

    private fun bindingOnClick() {
        binding.btnBackActivity.setOnClickListener {
            finish()
        }
    }

    private fun showPasswordEditResultMessage(result: Int) {
        when (result) {
            1 -> this.showCustomToast(getString(R.string.password_edit_toast_1))
            2 -> this.showCustomToast(getString(R.string.password_edit_toast_2))
            3 -> this.showCustomToast(getString(R.string.password_edit_toast_3))
            4 -> this.showCustomToast(getString(R.string.password_edit_toast_4))
            5 -> {
                this.showCustomToast(getString(R.string.password_edit_toast_5))
                val resultIntent = Intent()
                resultIntent.putExtra(getString(R.string.password_edit_key), viewModel.newPassword.value ?: "")
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }

            6 -> this.showCustomToast(getString(R.string.password_edit_toast_6))
        }
    }
}