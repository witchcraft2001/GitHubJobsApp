package ru.dm.githubpositions.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NavigationServiceImpl : NavigationService {
    private val _intent = MutableLiveData<NavigationIntent>()
    override val intent : LiveData<NavigationIntent> get() = _intent

    override fun navigate(intent: NavigationIntent) {
        _intent.postValue(intent)
    }
}