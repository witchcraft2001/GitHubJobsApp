package ru.dm.githubpositions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import ru.dm.githubpositions.fragments.list.ListFragment
import ru.dm.githubpositions.navigation.FragmentNavigationIntent
import ru.dm.githubpositions.navigation.NavigationIntent
import ru.dm.githubpositions.navigation.NavigationListener
import ru.dm.githubpositions.navigation.NavigationService
import ru.dm.githubpositions.utils.ActivityUtils

class MainActivity : AppCompatActivity() {
    private val navigationService: NavigationService by inject()
    private val activityUtils: ActivityUtils by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.mainContent, ListFragment()).commit()
        navigationService.setNavigationListener(object : NavigationListener {
            override fun onNavigate(intent: NavigationIntent) {
                when(intent) {
                    is FragmentNavigationIntent -> activityUtils.setCurrentFragment(this@MainActivity, intent.buildFragment(), true, intent.tag, false)
                }
            }
        })
    }
}