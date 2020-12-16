package ru.dm.githubpositions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.dm.githubpositions.fragments.details.DetailsFragment
import ru.dm.githubpositions.fragments.list.ListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.mainContent, ListFragment()).commit()
    }
}