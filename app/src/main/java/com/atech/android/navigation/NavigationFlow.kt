package com.atech.android.navigation

sealed class NavigationFlow {
    class LoginFlow(val title: String) : NavigationFlow()
    class HomeFlow(val title: String) : NavigationFlow()
}