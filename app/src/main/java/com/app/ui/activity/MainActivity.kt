package com.app.ui.activity

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.app.R
import com.app.databinding.ActivityMainBinding
import com.app.ui.activity.sign.LoginActivity
import com.core.ui.BaseActivity
import com.presentation.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val viewModel: MainActivityViewModel by viewModels()
    override fun setUi() {
        bindingNavigation()
    }

    override fun setUpDate() {

    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.checkLoggedInValue.observe(lifecycleOwner, ::checkLoggedIn)
    }

    private fun bindingNavigation() {
        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController() ?: return
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_navi_profile, R.id.menu_navi_friends, R.id.menu_navi_chat, R.id.menu_navi_schedule -> {
                    NavigationUI.onNavDestinationSelected(item, navController)
                    true
                }
                else -> false
            }
        }
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