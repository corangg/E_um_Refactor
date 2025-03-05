package com.app.ui.activity

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.databinding.ActivityMainBinding
import com.core.ui.BaseActivity
import com.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModels()
    override fun setUi() {
    }

    override fun setUpDate() {

    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.checkLoggedInValue.observe(lifecycleOwner, ::checkLoggedIn)
    }

    private fun checkLoggedIn(value: Boolean) {
        if (!value) startLoginActivity()
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}