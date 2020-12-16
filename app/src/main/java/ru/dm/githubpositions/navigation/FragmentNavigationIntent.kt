package ru.dm.githubpositions.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment

class FragmentNavigationIntent(clazz: Class<*>, bundle: Bundle?, val tag: String = clazz.simpleName) : NavigationIntent(clazz, bundle) {
    fun buildFragment() : Fragment {
        return (clazz.getConstructor().newInstance() as Fragment).apply {
            bundle?.let {
                this.arguments = bundle
            }
        }
    }
}