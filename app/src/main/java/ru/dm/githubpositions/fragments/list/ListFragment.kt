package ru.dm.githubpositions.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.dm.githubpositions.R
import ru.dm.githubpositions.adapters.PositionListAdapter
import ru.dm.githubpositions.adapters.listeners.AdapterOnClickListener
import ru.dm.githubpositions.data.models.Position
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
        setListeners()
        initAdapter()
        initState()
    }

    private fun setListeners() {
        btnRetry.setOnClickListener { listViewModel.retry() }
    }

    private fun initAdapter() {
        positionListAdapter = PositionListAdapter({ listViewModel.retry() },
            object : AdapterOnClickListener<Position> {
                override fun onClickItem(item: Position) {
                    listViewModel.itemClicked(item)
                }
            })
        rvItems.adapter = positionListAdapter
        listViewModel.positions.observe(viewLifecycleOwner, { positionListAdapter.submitList(it) })
    }

    private fun initState() {
        listViewModel.getState().observe(viewLifecycleOwner, Observer { state ->
            textNotFound.visibility =
                if (listViewModel.listIsEmpty() && state == State.DONE) VISIBLE else GONE
            progressBar.visibility =
                if (listViewModel.listIsEmpty() && state == State.LOADING) VISIBLE else GONE
            layoutError.visibility =
                if (listViewModel.listIsEmpty() && state == State.ERROR) VISIBLE else GONE
            if (!listViewModel.listIsEmpty()) {
                positionListAdapter.setState(state ?: State.DONE)
            }
        })
    }
}