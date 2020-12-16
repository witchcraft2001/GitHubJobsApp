package ru.dm.githubpositions.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.dm.githubpositions.R

class DetailsFragment : Fragment() {
    private val detailsViewModel: DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    companion object {
        const val POSITION_ID = "POSITION_ID"
        const val POSITION_TYPE = "POSITION_TYPE"
        const val POSITION_URL = "POSITION_URL"
        const val POSITION_COMPANY = "POSITION_COMPANY"
        const val POSITION_CREATED_AT = "POSITION_CREATED_AT"
        const val POSITION_LOCATION = "POSITION_LOCATION"
        const val POSITION_TITLE = "POSITION_TITLE"
        const val POSITION_DESCRIPTION = "POSITION_DESCRIPTION"
        const val POSITION_HOW_TO_APPLY = "POSITION_HOW_TO_APPLY"
        const val POSITION_COMPANY_URL = "POSITION_COMPANY_URL"
    }
}