package com.atech.android

import android.view.MenuItem
import androidx.activity.viewModels
import com.atech.android.base.BaseActivity
import com.atech.android.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override val viewModel: MainViewModel by viewModels()

    override fun onInitViews() = Unit

    override fun onInitObservers() = Unit


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (!navigator.navController.popBackStack())
                    finish()
                else
                    navigator.navController.navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}