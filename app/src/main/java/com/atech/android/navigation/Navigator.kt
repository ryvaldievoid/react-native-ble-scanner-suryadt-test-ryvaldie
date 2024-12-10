package com.atech.android.navigation

import androidx.navigation.NavController
import com.atech.android.navigation.NavigationFlow

class Navigator {
    lateinit var navController: NavController

    // navigate on main thread or nav component crashes sometimes
//    fun navigateToFlow(navigationFlow: NavigationFlow) = when (navigationFlow) {
//        is NavigationFlow.LoginFlow -> navController.navigate()
//        is NavigationFlow.HomeFlow -> navController.navigate(NavGraphMainDirections.actionGlobalNavGraphHome(navigationFlow.title))
//    }
}