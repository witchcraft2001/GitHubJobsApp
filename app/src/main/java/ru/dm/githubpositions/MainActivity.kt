package ru.dm.githubpositions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.koin.android.ext.android.inject
import ru.dm.githubpositions.fragments.list.ListFragment
import ru.dm.githubpositions.navigation.FragmentNavigationIntent
import ru.dm.githubpositions.navigation.NavigationService
import ru.dm.githubpositions.utils.ActivityUtils

class MainActivity : AppCompatActivity() {
    private val navigationService: NavigationService by inject()
    private val activityUtils: ActivityUtils by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.mainContent, ListFragment()).commit()
        navigationService.intent.observe(this, Observer { intent ->
            when(intent) {
                is FragmentNavigationIntent -> activityUtils.setCurrentFragment(this, intent.buildFragment(), true, intent.tag, false)
            }
        })
    }
}