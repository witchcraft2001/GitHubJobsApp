package ru.dm.githubpositions.fragments.list

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import ru.dm.githubpositions.data.GitHubPositionsService
import ru.dm.githubpositions.data.PositionsDataSource
import ru.dm.githubpositions.data.PositionsDataSourceFactory
import ru.dm.githubpositions.data.models.Position
import ru.dm.githubpositions.data.models.State
import ru.dm.githubpositions.fragments.details.DetailsFragment
import ru.dm.githubpositions.navigation.FragmentNavigationIntent
import ru.dm.githubpositions.navigation.NavigationService

class ListViewModel(
    private val networkService: GitHubPositionsService,
    private val navigationService: NavigationService
) : ViewModel() {

    val positions: LiveData<PagedList<Position>>

    private val positionsDataSourceFactory: PositionsDataSourceFactory

    private val compositeDisposable = CompositeDisposable()

    init {
        positionsDataSourceFactory = PositionsDataSourceFactory(compositeDisposable, networkService)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(50)
            .build()

        positions = LivePagedListBuilder(positionsDataSourceFactory, config).build()
    }

    fun itemClicked(position: Position) {
        val bundle = Bundle().apply {
            putString(DetailsFragment.POSITION_ID, position.id)
            putString(DetailsFragment.POSITION_TITLE, position.title)
            putString(DetailsFragment.POSITION_COMPANY, position.company)
            putString(DetailsFragment.POSITION_CREATED_AT, position.createdAt)
            putString(DetailsFragment.POSITION_DESCRIPTION, position.description)
            putString(DetailsFragment.POSITION_HOW_TO_APPLY, position.howToApply)
        }
        navigationService.navigate(FragmentNavigationIntent(DetailsFragment::class.java, bundle))
    }

    fun retry() {
        positionsDataSourceFactory.positionsDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return positions.value?.isEmpty() ?: true
    }

    fun getState(): LiveData<State> = Transformations.switchMap(
        positionsDataSourceFactory.positionsDataSourceLiveData,
        PositionsDataSource::state
    )

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}