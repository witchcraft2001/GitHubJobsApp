package ru.dm.githubpositions.navigation

class NavigationServiceImpl : NavigationService {

    private var _listener: NavigationListener? = null

    override fun navigate(intent: NavigationIntent) {
        _listener?.onNavigate(intent)
    }

    override fun setNavigationListener(listener: NavigationListener) {
        _listener = listener
    }
}