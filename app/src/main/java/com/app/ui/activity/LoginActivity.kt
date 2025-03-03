package com.app.ui.activity

import androidx.lifecycle.LifecycleOwner
import com.app.databinding.ActivityLogInBinding
import com.core.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLogInBinding>(ActivityLogInBinding::inflate) {
    override fun setUi() {
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
    }
}