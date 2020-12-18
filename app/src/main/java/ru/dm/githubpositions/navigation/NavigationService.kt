package ru.dm.githubpositions.navigation

interface NavigationService {
    fun navigate(intent: NavigationIntent)
    fun setNavigationListener(listener: NavigationListener)
}