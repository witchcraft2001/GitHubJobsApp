package ru.dm.githubpositions.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_list.*
import ru.dm.githubpositions.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.dm.githubpositions.adapters.PositionListAdapter
import ru.dm.githubpositions.data.models.State

class ListFragment : Fragment() {
    private val listViewModel: ListViewModel by viewModel()

    private lateinit var positionListAdapter: PositionListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initState()
    }

    private fun initAdapter() {
        positionListAdapter = PositionListAdapter { listViewModel.retry() }
        rvItems.adapter = positionListAdapter
        listViewModel.positions.observe(viewLifecycleOwner, Observer {
            positionListAdapter.submitList(it)
        })
    }

    private fun initState() {
        listViewModel.getState().observe(viewLifecycleOwner, Observer { state ->
            textNotFound.visibility = if (listViewModel.listIsEmpty() && state == State.DONE) VISIBLE else GONE
            progressBar.visibility =
                if (listViewModel.listIsEmpty() && state == State.LOADING) VISIBLE else GONE
            textError.visibility =
                if (listViewModel.listIsEmpty() && state == State.ERROR) VISIBLE else GONE
            if (!listViewModel.listIsEmpty()) {
                positionListAdapter.setState(state ?: State.DONE)
            }
        })
    }
}