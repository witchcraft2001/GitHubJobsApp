package ru.dm.githubpositions.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import ru.dm.githubpositions.R

class ActivityUtils {
    fun setCurrentFragment(
        activity: FragmentActivity,
        fragment: Fragment?,
        addToBackStack: Boolean,
        name: String?,
        useTransition: Boolean
    ) {
        val fm = activity.supportFragmentManager
        val ft = fm.beginTransaction()
        if (useTransition) {
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }
        ft.replace(R.id.mainContent, fragment!!, name)
        if (addToBackStack) {
            ft.addToBackStack(name)
        }

        ft.commitAllowingStateLoss()
    }
}