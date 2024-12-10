package com.atech.android.navigation.navigator

import androidx.navigation.NavController
import com.atech.android.navigation.navigator.BaseNavigator

class HomeNavigator constructor(override val navController: NavController) : BaseNavigator {

    fun navigateUp() = navController.navigateUp()
}