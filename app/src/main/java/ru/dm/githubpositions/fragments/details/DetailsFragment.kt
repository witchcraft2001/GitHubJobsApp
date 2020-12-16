package ru.dm.githubpositions.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.dm.githubpositions.R
import ru.dm.githubpositions.data.models.Position

class DetailsFragment : Fragment() {
    private val detailsViewModel: DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val position = Position(
                it.getString(POSITION_ID) ?: "",
                it.getString(POSITION_CREATED_AT) ?: "",
                it.getString(POSITION_COMPANY) ?: "",
                it.getString(POSITION_TITLE) ?: "",
                it.getString(POSITION_DESCRIPTION) ?: "",
                it.getString(POSITION_HOW_TO_APPLY) ?: "",
                it.getString(POSITION_COMPANY_LOGO) ?: ""
            )
            detailsViewModel.setModel(position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        detailsViewModel.position.observe(viewLifecycleOwner, Observer { position ->
            position.apply {
                tv_title.text = title
                tv_company.text = company
                tv_description.text = description
                tv_how_to_apply.text = howToApply
            }
        })
    }

    companion object {
        const val POSITION_ID = "POSITION_ID"
        const val POSITION_COMPANY = "POSITION_COMPANY"
        const val POSITION_CREATED_AT = "POSITION_CREATED_AT"
        const val POSITION_TITLE = "POSITION_TITLE"
        const val POSITION_DESCRIPTION = "POSITION_DESCRIPTION"
        const val POSITION_HOW_TO_APPLY = "POSITION_HOW_TO_APPLY"
        const val POSITION_COMPANY_LOGO = "POSITION_COMPANY_URL"
    }
}