package ru.dm.githubpositions.navigation

import androidx.lifecycle.LiveData

interface NavigationService {
    fun navigate(intent: NavigationIntent)
    val intent : LiveData<NavigationIntent>
}